package com.cityplatform.task.application;

import com.cityplatform.merchant.domain.StoreRepository;
import com.cityplatform.platform.exception.BusinessException;
import com.cityplatform.platform.exception.IllegalStatusTransitionException;
import com.cityplatform.platform.exception.NotFoundException;
import com.cityplatform.platform.web.PageParam;
import com.cityplatform.platform.web.PageResult;
import com.cityplatform.task.application.dto.ChangeTaskStatusRequest;
import com.cityplatform.task.application.dto.CompleteTaskRequest;
import com.cityplatform.task.application.dto.CreateTaskRequest;
import com.cityplatform.task.application.dto.TaskQuery;
import com.cityplatform.task.application.dto.UpdateTaskRequest;
import com.cityplatform.task.application.readmodel.TaskDetailReadModel;
import com.cityplatform.task.application.readmodel.TaskSummary;
import com.cityplatform.task.application.readmodel.UserTaskDetailReadModel;
import com.cityplatform.task.application.readmodel.UserTaskSummary;
import com.cityplatform.task.domain.RewardType;
import com.cityplatform.task.domain.Task;
import com.cityplatform.task.domain.TaskRepository;
import com.cityplatform.task.domain.TaskSource;
import com.cityplatform.task.domain.TaskStatus;
import com.cityplatform.task.domain.TaskType;
import com.cityplatform.task.domain.UserTask;
import com.cityplatform.task.domain.UserTaskRepository;
import com.cityplatform.user.application.UserApplicationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 任务应用服务。负责任务 CRUD、状态流转、用户参与与商户完成验证的用例编排。
 * 跨域协调在本层：Task Domain 不依赖 User/Merchant 任何外部 Domain；
 * 完成任务 + 发放积分在同一事务内（要么一起成功，要么一起失败）。
 */
@Service
public class TaskApplicationService {

    private static final Logger log = LoggerFactory.getLogger(TaskApplicationService.class);

    /** 任务核销码字符集：去除易混淆的 0/O/1/I */
    private static final String CODE_ALPHABET = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    private final TaskRepository taskRepository;
    private final TaskQueryRepository taskQueryRepository;
    private final UserTaskRepository userTaskRepository;
    private final UserTaskQueryRepository userTaskQueryRepository;
    private final StoreRepository storeRepository;
    private final UserApplicationService userApplicationService;

    public TaskApplicationService(TaskRepository taskRepository,
                                   TaskQueryRepository taskQueryRepository,
                                   UserTaskRepository userTaskRepository,
                                   UserTaskQueryRepository userTaskQueryRepository,
                                   StoreRepository storeRepository,
                                   UserApplicationService userApplicationService) {
        this.taskRepository = taskRepository;
        this.taskQueryRepository = taskQueryRepository;
        this.userTaskRepository = userTaskRepository;
        this.userTaskQueryRepository = userTaskQueryRepository;
        this.storeRepository = storeRepository;
        this.userApplicationService = userApplicationService;
    }

    // ==================== 任务 CRUD ====================

    /**
     * 创建任务。storeId 为可选弱关联（到店任务指向门店），
     * 提供时经 Merchant 域 Repository 校验存在性（跨域校验仅经 Repository 接口）。
     * TODO: 商户端登录态鉴权后，需校验操作权限。
     */
    @Transactional
    public TaskSummary create(CreateTaskRequest request) {
        validateStoreExistsIfPresent(request.getStoreId());
        Task task = buildTask(request);
        taskRepository.insert(task);
        return taskQueryRepository.findById(task.getId());
    }

    @Transactional
    public TaskSummary update(Long id, UpdateTaskRequest request) {
        Task task = loadTaskOrThrow(id);
        validateStoreExistsIfPresent(request.getStoreId());
        task.updateInfo(request.getTitle(), request.getDescription(),
                TaskType.valueOf(request.getTaskType()),
                TaskSource.valueOf(request.getSourceType()),
                request.getSourceId(), request.getStoreId(),
                RewardType.valueOf(request.getRewardType()), request.getRewardValue(),
                request.getStartAt(), request.getEndAt());
        taskRepository.update(task);
        return taskQueryRepository.findById(id);
    }

    /**
     * 状态变更。合法流转仅 DRAFT -> ACTIVE 与 ACTIVE -> DISABLED。
     */
    @Transactional
    public TaskSummary changeStatus(Long id, ChangeTaskStatusRequest request) {
        Task task = loadTaskOrThrow(id);
        TaskStatus target = TaskStatus.valueOf(request.getStatus());
        if (target == TaskStatus.ACTIVE) {
            task.activate();
        } else if (target == TaskStatus.DISABLED) {
            task.disable();
        } else {
            throw new IllegalStatusTransitionException("不支持流转为状态: " + target);
        }
        taskRepository.update(task);
        return taskQueryRepository.findById(id);
    }

    // ==================== 查询 ====================

    public TaskSummary get(Long id) {
        TaskSummary rm = taskQueryRepository.findById(id);
        if (rm == null) {
            throw new NotFoundException("任务不存在: " + id);
        }
        fillEffectiveStatus(rm);
        return rm;
    }

    /**
     * 任务详情。携带有效登录态时附带当前用户参与状态（myStatus）。
     * 用户公开视图（management=false）仅返回启用(ACTIVE)任务，
     * 未启用/已停用按不存在处理；管理视图（management=true）可查看全部状态，
     * 待商户/运营端鉴权接入后收紧为登录态校验。
     */
    public TaskDetailReadModel getDetail(Long id, Long userId, boolean management) {
        TaskDetailReadModel rm = taskQueryRepository.findDetailById(id);
        if (rm == null) {
            throw new NotFoundException("任务不存在: " + id);
        }
        if (!management && !TaskStatus.ACTIVE.name().equals(rm.getStatus())) {
            throw new NotFoundException("任务不存在: " + id);
        }
        fillEffectiveStatus(rm);
        if (userId != null) {
            rm.setMyStatus(userTaskQueryRepository.findMyStatus(userId, id));
        }
        return rm;
    }

    public PageResult<TaskSummary> list(TaskQuery query, PageParam page) {
        List<TaskSummary> data = taskQueryRepository.findAll(query, page);
        data.forEach(this::fillEffectiveStatus);
        long total = taskQueryRepository.count(query);
        return new PageResult<>(data, total, page.getPage(), page.getSize());
    }

    // ==================== 用户侧：参与 ====================

    /**
     * 用户参与任务。userId 一律来自平台登录态。
     * 规则：仅 ACTIVE 且在时间窗口内的任务可参与；一次性任务不可重复参与，
     * 由 Application 校验 + 数据库 UNIQUE(user_id, task_id) 双重保证。
     */
    @Transactional
    public UserTaskDetailReadModel join(Long taskId, Long userId) {
        Task task = loadTaskOrThrow(taskId);
        if (!task.joinable()) {
            LocalDateTime now = LocalDateTime.now();
            if (task.getStatus() != TaskStatus.ACTIVE) {
                throw new BusinessException("TASK_NOT_JOINABLE", "任务当前状态不可参与");
            }
            if (now.isBefore(task.getStartAt())) {
                throw new BusinessException("TASK_NOT_STARTED", "任务未开始");
            }
            throw new BusinessException("TASK_ENDED", "任务已结束");
        }
        UserTask existing = userTaskRepository.findByUserIdAndTaskId(userId, taskId);
        if (existing != null) {
            throw new BusinessException("TASK_ALREADY_JOINED", "您已参与过该任务");
        }
        UserTask userTask = UserTask.join(taskId, userId, generateTaskCode());
        try {
            userTaskRepository.insert(userTask);
        } catch (DuplicateKeyException e) {
            // 并发参与兜底：UNIQUE(user_id, task_id) 冲突转为业务错误而非 500
            throw new BusinessException("TASK_ALREADY_JOINED", "您已参与过该任务");
        }
        log.info("用户参与任务: userId={}, taskId={}, taskCode={}",
                userId, taskId, userTask.getTaskCode());
        return getMyTaskDetail(userId, userTask.getId());
    }

    /**
     * 我的任务列表（含进行中/已完成，由前端按 taskStatus 区分展示）。
     * taskEffectiveStatus 由 SQL 读时计算任务存储态+时间窗口。
     */
    public PageResult<UserTaskSummary> myTasks(Long userId, PageParam page) {
        List<UserTaskSummary> data = userTaskQueryRepository.findMyTasks(userId, page);
        long total = userTaskQueryRepository.countMyTasks(userId);
        return new PageResult<>(data, total, page.getPage(), page.getSize());
    }

    /**
     * 我的任务详情（含任务核销码）。仅本人可查，查询条件含 userId。
     */
    public UserTaskDetailReadModel getMyTaskDetail(Long userId, Long userTaskId) {
        UserTaskDetailReadModel rm = userTaskQueryRepository.findMyTaskDetail(userId, userTaskId);
        if (rm == null) {
            throw new NotFoundException("任务参与记录不存在: " + userTaskId);
        }
        return rm;
    }

    // ==================== 商户侧：完成验证 + 奖励 ====================

    /**
     * 商户完成任务验证（输入任务核销码，兼容扫码枪键盘输入）。
     * 必须同时满足：UserTask=JOINED、Task=ACTIVE、当前在时间窗口内、核销码匹配。
     * 完成与积分发放位于同一事务：要么一起提交，要么一起失败；
     * 重复完成通过"先校验后条件更新"双重防护。
     */
    @Transactional
    public UserTaskDetailReadModel complete(CompleteTaskRequest request) {
        String taskCode = request.getTaskCode() == null ? "" : request.getTaskCode().trim().toUpperCase();
        UserTask userTask = userTaskRepository.findByTaskCode(taskCode);
        if (userTask == null) {
            throw new NotFoundException("任务核销码不存在: " + taskCode);
        }
        Task task = loadTaskOrThrow(userTask.getTaskId());

        if (!userTask.canComplete()) {
            throw new BusinessException("TASK_ALREADY_COMPLETED", "任务已完成，不可重复完成");
        }
        if (task.getStatus() != TaskStatus.ACTIVE) {
            throw new BusinessException("TASK_NOT_ACTIVE", "任务未启用");
        }
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(task.getStartAt())) {
            throw new BusinessException("TASK_NOT_STARTED", "任务未开始");
        }
        if (now.isAfter(task.getEndAt())) {
            throw new BusinessException("TASK_ENDED", "任务已结束");
        }

        userTask.complete();
        int updated = userTaskRepository.markCompleted(userTask);
        if (updated == 0) {
            // 并发完成兜底：条件更新未命中说明已完成
            throw new BusinessException("TASK_ALREADY_COMPLETED", "任务已完成，不可重复完成");
        }

        // 发放奖励：经 User 域 Application 能力（本事务内），Task Domain 不跨域修改 User
        if (userTask.rewardPending(task.getRewardType(), task.getRewardValue())) {
            userApplicationService.addPoints(userTask.getUserId(), task.getRewardValue());
            userTask.markRewardIssued();
            int marked = userTaskRepository.markRewardIssued(userTask.getId());
            if (marked == 0) {
                throw new BusinessException("TASK_REWARD_ALREADY_ISSUED", "任务奖励已发放");
            }
            log.info("任务奖励发放: userId={}, taskId={}, points={}",
                    userTask.getUserId(), task.getId(), task.getRewardValue());
        }
        log.info("任务完成验证: taskCode={}, userTaskId={}, taskId={}",
                taskCode, userTask.getId(), task.getId());
        return getMyTaskDetail(userTask.getUserId(), userTask.getId());
    }

    // ==================== 私有辅助 ====================

    private Task buildTask(CreateTaskRequest request) {
        return Task.create(request.getTitle(), request.getDescription(),
                TaskType.valueOf(request.getTaskType()),
                TaskSource.valueOf(request.getSourceType()),
                request.getSourceId(), request.getStoreId(),
                RewardType.valueOf(request.getRewardType()), request.getRewardValue(),
                request.getStartAt(), request.getEndAt());
    }

    private Task loadTaskOrThrow(Long id) {
        Task task = taskRepository.findById(id);
        if (task == null) {
            throw new NotFoundException("任务不存在: " + id);
        }
        return task;
    }

    /**
     * storeId 为可选弱关联，提供时校验存在性（跨域仅经 Repository 接口）。
     */
    private void validateStoreExistsIfPresent(Long storeId) {
        if (storeId != null && storeRepository.findById(storeId) == null) {
            throw new NotFoundException("门店不存在: " + storeId);
        }
    }

    /**
     * 生成任务核销码：TK + 8 位随机（无易混淆字符，SecureRandom）。
     */
    private String generateTaskCode() {
        StringBuilder sb = new StringBuilder("TK");
        for (int i = 0; i < 8; i++) {
            sb.append(CODE_ALPHABET.charAt(RANDOM.nextInt(CODE_ALPHABET.length())));
        }
        return sb.toString();
    }

    private void fillEffectiveStatus(TaskSummary rm) {
        LocalDateTime now = LocalDateTime.now();
        if (!"ACTIVE".equals(rm.getStatus())) {
            rm.setEffectiveStatus(rm.getStatus());
        } else if (rm.getStartAt() != null && now.isBefore(rm.getStartAt())) {
            rm.setEffectiveStatus("PENDING");
        } else if (rm.getEndAt() != null && now.isAfter(rm.getEndAt())) {
            rm.setEffectiveStatus("ENDED");
        } else {
            rm.setEffectiveStatus("ACTIVE");
        }
    }
}
