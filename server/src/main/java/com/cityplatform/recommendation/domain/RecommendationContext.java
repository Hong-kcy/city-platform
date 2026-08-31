package com.cityplatform.recommendation.domain;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * 推荐上下文（Domain 纯输入对象）。
 * 由 Application 层从登录用户与请求参数整理而来，Domain 不感知 User 实体。
 * 兴趣标签均来自用户轻量画像字段解析（大写），为空集表示无个性化信息。
 */
public class RecommendationContext {

    private final LocalDateTime now;
    /** 偏好活动类型集合（ActivityType 值），用于活动兴趣匹配 */
    private final Set<String> activityInterests;
    /** 偏好体验类型集合（RouteTheme 值等），用于路线主题匹配 */
    private final Set<String> experienceInterests;
    /** 合并兴趣标签集合（含商户品类 FOOD/ENTERTAINMENT 等），用于门店品类匹配 */
    private final Set<String> generalInterests;
    /** 距离锚点纬度；null 表示本批次不启用距离评分 */
    private final Double anchorLatitude;
    /** 距离锚点经度；null 表示本批次不启用距离评分 */
    private final Double anchorLongitude;

    public RecommendationContext(LocalDateTime now,
                                 Set<String> activityInterests,
                                 Set<String> experienceInterests,
                                 Set<String> generalInterests,
                                 Double anchorLatitude, Double anchorLongitude) {
        this.now = now;
        this.activityInterests = activityInterests;
        this.experienceInterests = experienceInterests;
        this.generalInterests = generalInterests;
        this.anchorLatitude = anchorLatitude;
        this.anchorLongitude = anchorLongitude;
    }

    public LocalDateTime getNow() { return now; }
    public Set<String> getActivityInterests() { return activityInterests; }
    public Set<String> getExperienceInterests() { return experienceInterests; }
    public Set<String> getGeneralInterests() { return generalInterests; }
    public Double getAnchorLatitude() { return anchorLatitude; }
    public Double getAnchorLongitude() { return anchorLongitude; }

    public boolean hasAnchor() {
        return anchorLatitude != null && anchorLongitude != null;
    }
}
