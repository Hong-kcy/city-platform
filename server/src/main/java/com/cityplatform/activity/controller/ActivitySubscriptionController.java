package com.cityplatform.activity.controller;

import com.cityplatform.activity.application.ActivityApplicationService;
import com.cityplatform.activity.application.readmodel.ActivitySubscriptionReadModel;
import com.cityplatform.activity.application.readmodel.MyActivitySubscription;
import com.cityplatform.platform.authentication.AuthenticationInterceptor;
import com.cityplatform.platform.authentication.CurrentUser;
import com.cityplatform.platform.web.PageParam;
import com.cityplatform.platform.web.PageResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 活动订阅 Controller（用户"想去"）。
 * userId 一律来自平台登录态（拦截器写入的请求属性），不接受前端传入 userId。
 * 注意：业务订阅与微信订阅消息授权是两个概念，本接口仅处理业务订阅。
 */
@RestController
public class ActivitySubscriptionController {

    private final ActivityApplicationService service;

    public ActivitySubscriptionController(ActivityApplicationService service) {
        this.service = service;
    }

    @PostMapping("/api/activities/{activityId}/subscription")
    public ActivitySubscriptionReadModel subscribe(
            @PathVariable Long activityId,
            @RequestAttribute(name = AuthenticationInterceptor.CURRENT_USER_ATTRIBUTE,
                    required = false) CurrentUser currentUser) {
        return service.subscribe(activityId, CurrentUser.required(currentUser).userId());
    }

    @DeleteMapping("/api/activities/{activityId}/subscription")
    public ActivitySubscriptionReadModel cancel(
            @PathVariable Long activityId,
            @RequestAttribute(name = AuthenticationInterceptor.CURRENT_USER_ATTRIBUTE,
                    required = false) CurrentUser currentUser) {
        return service.cancelSubscription(activityId, CurrentUser.required(currentUser).userId());
    }

    @GetMapping("/api/activities/{activityId}/subscription")
    public ActivitySubscriptionReadModel status(
            @PathVariable Long activityId,
            @RequestAttribute(name = AuthenticationInterceptor.CURRENT_USER_ATTRIBUTE,
                    required = false) CurrentUser currentUser) {
        return service.getSubscriptionStatus(activityId, CurrentUser.required(currentUser).userId());
    }

    @GetMapping("/api/users/me/activity-subscriptions")
    public PageResult<MyActivitySubscription> mySubscriptions(
            @RequestAttribute(name = AuthenticationInterceptor.CURRENT_USER_ATTRIBUTE,
                    required = false) CurrentUser currentUser,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        return service.mySubscriptions(CurrentUser.required(currentUser).userId(),
                new PageParam(page, size));
    }
}
