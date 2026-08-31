package com.cityplatform.task.controller;

import com.cityplatform.platform.authentication.AuthenticationInterceptor;
import com.cityplatform.platform.authentication.CurrentUser;
import com.cityplatform.platform.web.PageParam;
import com.cityplatform.platform.web.PageResult;
import com.cityplatform.task.application.TaskApplicationService;
import com.cityplatform.task.application.dto.ChangeTaskStatusRequest;
import com.cityplatform.task.application.dto.CompleteTaskRequest;
import com.cityplatform.task.application.dto.CreateTaskRequest;
import com.cityplatform.task.application.dto.TaskQuery;
import com.cityplatform.task.application.dto.UpdateTaskRequest;
import com.cityplatform.task.application.readmodel.TaskDetailReadModel;
import com.cityplatform.task.application.readmodel.TaskSummary;
import com.cityplatform.task.application.readmodel.UserTaskDetailReadModel;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 任务 Controller（管理/查询/完成验证共用，与活动接口风格一致）。
 * 列表/详情匿名可访问；详情在携带登录态时附带当前用户参与状态。
 * 管理端写操作 TODO: 商户/运营端登录态鉴权（当前沿用项目现有最小边界）。
 */
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskApplicationService service;

    public TaskController(TaskApplicationService service) {
        this.service = service;
    }

    @PostMapping
    public TaskSummary create(@Valid @RequestBody CreateTaskRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public TaskSummary update(@PathVariable Long id,
                              @Valid @RequestBody UpdateTaskRequest request) {
        return service.update(id, request);
    }

    /**
     * 任务详情。默认为用户公开视图，仅返回启用(ACTIVE)任务；
     * 商户/运营管理端携带 management=true 查看全部状态，待鉴权接入后收紧。
     */
    @GetMapping("/{id}")
    public TaskDetailReadModel getDetail(
            @PathVariable Long id,
            @RequestParam(defaultValue = "false") boolean management,
            @RequestAttribute(name = AuthenticationInterceptor.CURRENT_USER_ATTRIBUTE,
                    required = false) CurrentUser currentUser) {
        return service.getDetail(id, currentUser == null ? null : currentUser.userId(), management);
    }

    @GetMapping
    public PageResult<TaskSummary> list(
            @RequestParam(required = false) Long storeId,
            @RequestParam(required = false) String sourceType,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        TaskQuery query = new TaskQuery();
        query.setStoreId(storeId);
        query.setSourceType(sourceType);
        query.setStatus(status);
        return service.list(query, new PageParam(page, size));
    }

    @PatchMapping("/{id}/status")
    public TaskSummary changeStatus(@PathVariable Long id,
                                    @Valid @RequestBody ChangeTaskStatusRequest request) {
        return service.changeStatus(id, request);
    }

    /**
     * 商户完成任务验证（Web 后台输入任务核销码，兼容扫码枪键盘输入）。
     * 完成与积分发放在同一事务内。
     * TODO: 商户端登录态鉴权后，操作门店改由登录态提供。
     */
    @PostMapping("/complete")
    public UserTaskDetailReadModel complete(@Valid @RequestBody CompleteTaskRequest request) {
        return service.complete(request);
    }
}
