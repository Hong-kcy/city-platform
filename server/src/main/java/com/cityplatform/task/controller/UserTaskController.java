package com.cityplatform.task.controller;

import com.cityplatform.platform.authentication.AuthenticationInterceptor;
import com.cityplatform.platform.authentication.CurrentUser;
import com.cityplatform.platform.web.PageParam;
import com.cityplatform.platform.web.PageResult;
import com.cityplatform.task.application.TaskApplicationService;
import com.cityplatform.task.application.readmodel.UserTaskDetailReadModel;
import com.cityplatform.task.application.readmodel.UserTaskSummary;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户任务 Controller（参与/我的任务）。
 * userId 一律来自平台登录态（拦截器写入的请求属性），不接受前端传入 userId。
 */
@RestController
public class UserTaskController {

    private final TaskApplicationService service;

    public UserTaskController(TaskApplicationService service) {
        this.service = service;
    }

    @PostMapping("/api/tasks/{taskId}/join")
    public UserTaskDetailReadModel join(
            @PathVariable Long taskId,
            @RequestAttribute(name = AuthenticationInterceptor.CURRENT_USER_ATTRIBUTE,
                    required = false) CurrentUser currentUser) {
        return service.join(taskId, CurrentUser.required(currentUser).userId());
    }

    @GetMapping("/api/users/me/tasks")
    public PageResult<UserTaskSummary> myTasks(
            @RequestAttribute(name = AuthenticationInterceptor.CURRENT_USER_ATTRIBUTE,
                    required = false) CurrentUser currentUser,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        return service.myTasks(CurrentUser.required(currentUser).userId(),
                new PageParam(page, size));
    }

    @GetMapping("/api/users/me/tasks/{id}")
    public UserTaskDetailReadModel myTaskDetail(
            @PathVariable Long id,
            @RequestAttribute(name = AuthenticationInterceptor.CURRENT_USER_ATTRIBUTE,
                    required = false) CurrentUser currentUser) {
        return service.getMyTaskDetail(CurrentUser.required(currentUser).userId(), id);
    }
}
