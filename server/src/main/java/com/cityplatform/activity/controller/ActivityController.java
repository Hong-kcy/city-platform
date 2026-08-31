package com.cityplatform.activity.controller;

import com.cityplatform.activity.application.ActivityApplicationService;
import com.cityplatform.activity.application.dto.ActivityQuery;
import com.cityplatform.activity.application.dto.ChangeActivityStatusRequest;
import com.cityplatform.activity.application.dto.CreateActivityRequest;
import com.cityplatform.activity.application.dto.UpdateActivityRequest;
import com.cityplatform.activity.application.readmodel.ActivityDetailReadModel;
import com.cityplatform.activity.application.readmodel.ActivitySummary;
import com.cityplatform.platform.authentication.AuthenticationInterceptor;
import com.cityplatform.platform.authentication.CurrentUser;
import com.cityplatform.platform.web.PageParam;
import com.cityplatform.platform.web.PageResult;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
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

import java.time.LocalDateTime;

/**
 * 活动 Controller。仅负责接入 HTTP、参数校验、调用 ApplicationService。
 * 列表/详情匿名可访问；详情在携带登录态时附带当前用户订阅状态。
 */
@RestController
@RequestMapping("/api/activities")
public class ActivityController {

    private final ActivityApplicationService service;

    public ActivityController(ActivityApplicationService service) {
        this.service = service;
    }

    @PostMapping
    public ActivitySummary create(@Valid @RequestBody CreateActivityRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public ActivitySummary update(@PathVariable Long id,
                                  @Valid @RequestBody UpdateActivityRequest request) {
        return service.update(id, request);
    }

    /**
     * 活动详情。默认为用户公开视图，仅返回已发布活动；
     * 商户/运营管理端携带 management=true 查看全部状态（含 DRAFT/OFFLINE），
     * 待商户端鉴权接入后收紧为登录态校验。
     */
    @GetMapping("/{id}")
    public ActivityDetailReadModel getDetail(
            @PathVariable Long id,
            @RequestParam(defaultValue = "false") boolean management,
            @RequestAttribute(name = AuthenticationInterceptor.CURRENT_USER_ATTRIBUTE,
                    required = false) CurrentUser currentUser) {
        return service.getDetail(id, currentUser == null ? null : currentUser.userId(), management);
    }

    @GetMapping
    public PageResult<ActivitySummary> list(
            @RequestParam(required = false) Long streetAreaId,
            @RequestParam(required = false) String activityType,
            @RequestParam(required = false) String status,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTimeFrom,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTimeTo,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        ActivityQuery query = new ActivityQuery();
        query.setStreetAreaId(streetAreaId);
        query.setActivityType(activityType);
        query.setStatus(status);
        query.setStartTimeFrom(startTimeFrom);
        query.setStartTimeTo(startTimeTo);
        return service.list(query, new PageParam(page, size));
    }

    @PatchMapping("/{id}/status")
    public ActivitySummary changeStatus(@PathVariable Long id,
                                        @Valid @RequestBody ChangeActivityStatusRequest request) {
        return service.changeStatus(id, request);
    }
}
