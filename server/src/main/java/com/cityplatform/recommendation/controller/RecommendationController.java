package com.cityplatform.recommendation.controller;

import com.cityplatform.platform.authentication.AuthenticationInterceptor;
import com.cityplatform.platform.authentication.CurrentUser;
import com.cityplatform.recommendation.application.RecommendationApplicationService;
import com.cityplatform.recommendation.application.readmodel.TodayRecommendationReadModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 推荐 Controller。只负责 HTTP，推荐计算全部在 Application/Domain。
 * 登录态可选：未登录仍返回非个性化推荐；用户身份一律取自平台登录态，
 * 不接受前端传 userId。latitude/longitude 为可选距离锚点，
 * 未提供时使用 streetAreaId 街区中心（Demo 默认位置策略，不接入地图定位）。
 */
@RestController
public class RecommendationController {

    private final RecommendationApplicationService service;

    public RecommendationController(RecommendationApplicationService service) {
        this.service = service;
    }

    @GetMapping("/api/recommendations/today")
    public TodayRecommendationReadModel today(
            @RequestAttribute(name = AuthenticationInterceptor.CURRENT_USER_ATTRIBUTE,
                    required = false) CurrentUser currentUser,
            @RequestParam(required = false) Long streetAreaId,
            @RequestParam(required = false) Double latitude,
            @RequestParam(required = false) Double longitude) {
        Long userId = currentUser == null ? null : currentUser.userId();
        return service.today(userId, streetAreaId, latitude, longitude);
    }
}
