package com.cityplatform.task.domain;

import com.cityplatform.platform.exception.BusinessException;
import com.cityplatform.platform.exception.IllegalStatusTransitionException;

import java.time.LocalDateTime;

/**
 * 任务 Entity。独立业务域，拥有自己的生命周期，不从属于 Activity/Recommendation/
 * Operation/Merchant 任何一方。sourceType/sourceId 仅为来源引用元数据（弱关联），
 * 不建立强实体依赖。到店任务 Demo 阶段采用核销码验证，不做 GPS/围栏判断。
 * 状态 DRAFT -> ACTIVE -> DISABLED 由本 Entity 维护；用户完成态由 UserTask 表达。
 */
public class Task {

    private Long id;
    private String title;
    private String description;
    private TaskType taskType;
    private TaskSource sourceType;
    private Long sourceId;
    private Long storeId;
    private RewardType rewardType;
    private int rewardValue;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private TaskStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * 工厂方法：创建任务。新任务默认 DRAFT，需启用后对用户可见可参与。
     */
    public static Task create(String title, String description, TaskType taskType,
                              TaskSource sourceType, Long sourceId, Long storeId,
                              RewardType rewardType, int rewardValue,
                              LocalDateTime startAt, LocalDateTime endAt) {
        validateTimeRange(startAt, endAt);
        if (rewardValue < 0) {
            throw new BusinessException("INVALID_TASK_REWARD", "任务奖励数值不能为负");
        }
        Task t = new Task();
        t.title = title;
        t.description = description;
        t.taskType = taskType;
        t.sourceType = sourceType;
        t.sourceId = sourceId;
        t.storeId = storeId;
        t.rewardType = rewardType;
        t.rewardValue = rewardValue;
        t.startAt = startAt;
        t.endAt = endAt;
        t.status = TaskStatus.DRAFT;
        LocalDateTime now = LocalDateTime.now();
        t.createdAt = now;
        t.updatedAt = now;
        return t;
    }

    /**
     * 修改任务信息（PUT 全量替换语义）。已停用任务不允许再修改。
     */
    public void updateInfo(String title, String description, TaskType taskType,
                           TaskSource sourceType, Long sourceId, Long storeId,
                           RewardType rewardType, int rewardValue,
                           LocalDateTime startAt, LocalDateTime endAt) {
        if (this.status == TaskStatus.DISABLED) {
            throw new IllegalStatusTransitionException("已停用任务不允许修改");
        }
        validateTimeRange(startAt, endAt);
        if (rewardValue < 0) {
            throw new BusinessException("INVALID_TASK_REWARD", "任务奖励数值不能为负");
        }
        this.title = title;
        this.description = description;
        this.taskType = taskType;
        this.sourceType = sourceType;
        this.sourceId = sourceId;
        this.storeId = storeId;
        this.rewardType = rewardType;
        this.rewardValue = rewardValue;
        this.startAt = startAt;
        this.endAt = endAt;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 启用：仅 DRAFT -> ACTIVE。
     */
    public void activate() {
        if (this.status != TaskStatus.DRAFT) {
            throw new IllegalStatusTransitionException("仅草稿状态可启用，当前状态: " + this.status);
        }
        this.status = TaskStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 停用：仅 ACTIVE -> DISABLED。软操作，已有参与记录保留。
     */
    public void disable() {
        if (this.status != TaskStatus.ACTIVE) {
            throw new IllegalStatusTransitionException("仅已启用状态可停用，当前状态: " + this.status);
        }
        this.status = TaskStatus.DISABLED;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Domain 规则：是否可被用户参与。需 ACTIVE 且当前时间在任务时间窗口内。
     */
    public boolean joinable() {
        LocalDateTime now = LocalDateTime.now();
        return status == TaskStatus.ACTIVE
                && !now.isBefore(startAt)
                && !now.isAfter(endAt);
    }

    /**
     * Domain 规则：是否可完成验证。需 ACTIVE 且在时间窗口内。
     */
    public boolean completable() {
        return joinable();
    }

    /**
     * Domain 规则：计算读时有效状态：PENDING/ACTIVE/ENDED/DISABLED/DRAFT。
     */
    public String effectiveStatus() {
        if (status != TaskStatus.ACTIVE) {
            return status.name();
        }
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(startAt)) {
            return "PENDING";
        }
        if (now.isAfter(endAt)) {
            return "ENDED";
        }
        return TaskStatus.ACTIVE.name();
    }

    private static void validateTimeRange(LocalDateTime startAt, LocalDateTime endAt) {
        if (startAt == null || endAt == null || !endAt.isAfter(startAt)) {
            throw new BusinessException("INVALID_TASK_TIME", "任务结束时间必须晚于开始时间");
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public TaskType getTaskType() { return taskType; }
    public void setTaskType(TaskType taskType) { this.taskType = taskType; }
    public TaskSource getSourceType() { return sourceType; }
    public void setSourceType(TaskSource sourceType) { this.sourceType = sourceType; }
    public Long getSourceId() { return sourceId; }
    public void setSourceId(Long sourceId) { this.sourceId = sourceId; }
    public Long getStoreId() { return storeId; }
    public void setStoreId(Long storeId) { this.storeId = storeId; }
    public RewardType getRewardType() { return rewardType; }
    public void setRewardType(RewardType rewardType) { this.rewardType = rewardType; }
    public int getRewardValue() { return rewardValue; }
    public void setRewardValue(int rewardValue) { this.rewardValue = rewardValue; }
    public LocalDateTime getStartAt() { return startAt; }
    public void setStartAt(LocalDateTime startAt) { this.startAt = startAt; }
    public LocalDateTime getEndAt() { return endAt; }
    public void setEndAt(LocalDateTime endAt) { this.endAt = endAt; }
    public TaskStatus getStatus() { return status; }
    public void setStatus(TaskStatus status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
