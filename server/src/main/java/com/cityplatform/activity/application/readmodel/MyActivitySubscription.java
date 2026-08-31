package com.cityplatform.activity.application.readmodel;

import java.time.LocalDateTime;

/**
 * "我的活动"列表项查询模型。订阅信息 + 活动摘要（JOIN 组装，避免 N+1）。
 * activityStatus 供前端区分活动已下线等情况。
 */
public class MyActivitySubscription {

    private Long subscriptionId;
    private Long activityId;
    private String title;
    private String summary;
    private String coverImageUrl;
    private String activityType;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String activityStatus;
    private String streetAreaName;
    private String subscriptionStatus;
    private LocalDateTime subscribedAt;

    public Long getSubscriptionId() { return subscriptionId; }
    public void setSubscriptionId(Long subscriptionId) { this.subscriptionId = subscriptionId; }
    public Long getActivityId() { return activityId; }
    public void setActivityId(Long activityId) { this.activityId = activityId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }
    public String getCoverImageUrl() { return coverImageUrl; }
    public void setCoverImageUrl(String coverImageUrl) { this.coverImageUrl = coverImageUrl; }
    public String getActivityType() { return activityType; }
    public void setActivityType(String activityType) { this.activityType = activityType; }
    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    public String getActivityStatus() { return activityStatus; }
    public void setActivityStatus(String activityStatus) { this.activityStatus = activityStatus; }
    public String getStreetAreaName() { return streetAreaName; }
    public void setStreetAreaName(String streetAreaName) { this.streetAreaName = streetAreaName; }
    public String getSubscriptionStatus() { return subscriptionStatus; }
    public void setSubscriptionStatus(String subscriptionStatus) { this.subscriptionStatus = subscriptionStatus; }
    public LocalDateTime getSubscribedAt() { return subscribedAt; }
    public void setSubscribedAt(LocalDateTime subscribedAt) { this.subscribedAt = subscribedAt; }
}
