package com.cityplatform.recommendation.domain;

import java.time.LocalDateTime;

/**
 * 推荐候选（Domain 纯输入对象）。
 * 由 Application 层从各业务域聚合整理而来，Domain 不感知 Store/Activity/ExperienceRoute 等实体：
 * - STORE 候选：categoryTag = 商户类型(FOOD/ENTERTAINMENT/...)，hasCoupon/distanceMeters 有值
 * - ACTIVITY 候选：categoryTag = 活动类型(FESTIVAL/PERFORMANCE/...)，startTime/endTime 有值
 * - EXPERIENCE_ROUTE 候选：categoryTag = 路线主题(FRIEND_PHOTO/...)，estimatedDurationMinutes 有值
 * 候选进入 Domain 前已完成硬过滤：门店营业中、活动未结束、内容未停用。
 */
public class RecommendationCandidate {

    private RecommendationType type;
    private Long targetId;
    private String title;
    private String categoryTag;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer estimatedDurationMinutes;
    private boolean hasCoupon;
    private Double distanceMeters;

    public RecommendationCandidate(RecommendationType type, Long targetId, String title, String categoryTag) {
        this.type = type;
        this.targetId = targetId;
        this.title = title;
        this.categoryTag = categoryTag;
    }

    public RecommendationType getType() { return type; }
    public Long getTargetId() { return targetId; }
    public String getTitle() { return title; }
    public String getCategoryTag() { return categoryTag; }
    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    public Integer getEstimatedDurationMinutes() { return estimatedDurationMinutes; }
    public void setEstimatedDurationMinutes(Integer estimatedDurationMinutes) { this.estimatedDurationMinutes = estimatedDurationMinutes; }
    public boolean isHasCoupon() { return hasCoupon; }
    public void setHasCoupon(boolean hasCoupon) { this.hasCoupon = hasCoupon; }
    public Double getDistanceMeters() { return distanceMeters; }
    public void setDistanceMeters(Double distanceMeters) { this.distanceMeters = distanceMeters; }
}
