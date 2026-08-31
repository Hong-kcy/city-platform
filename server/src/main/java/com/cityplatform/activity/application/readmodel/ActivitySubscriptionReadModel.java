package com.cityplatform.activity.application.readmodel;

import java.time.LocalDateTime;

/**
 * 活动订阅查询模型（单个活动视角）。
 * 未订阅时 status 为 null；subscribed 由 status 派生。
 */
public class ActivitySubscriptionReadModel {

    private Long activityId;
    private Long userId;
    /** ACTIVE/CANCELLED，未订阅为 null */
    private String status;
    private LocalDateTime createdAt;

    public ActivitySubscriptionReadModel() {
    }

    public ActivitySubscriptionReadModel(Long activityId, Long userId) {
        this.activityId = activityId;
        this.userId = userId;
    }

    /** 是否为生效订阅（供前端渲染"想去"按钮态） */
    public boolean isSubscribed() {
        return "ACTIVE".equals(status);
    }

    public Long getActivityId() { return activityId; }
    public void setActivityId(Long activityId) { this.activityId = activityId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
