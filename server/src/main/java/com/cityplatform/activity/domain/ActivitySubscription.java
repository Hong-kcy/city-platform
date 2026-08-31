package com.cityplatform.activity.domain;

import com.cityplatform.platform.exception.IllegalStatusTransitionException;

import java.time.LocalDateTime;

/**
 * 活动订阅 Entity（用户"想去"）。独立于 Activity 的模型，不作为 Activity 的附属集合，
 * 便于后续支持取消订阅、通知、订阅统计、用户活动列表。
 * 一个用户对同一活动仅一条记录（数据库 UNIQUE(user_id, activity_id) 兜底）。
 */
public class ActivitySubscription {

    private Long id;
    private Long activityId;
    private Long userId;
    private SubscriptionStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * 工厂方法：用户订阅活动（点击"想去"）。
     */
    public static ActivitySubscription subscribe(Long activityId, Long userId) {
        ActivitySubscription s = new ActivitySubscription();
        s.activityId = activityId;
        s.userId = userId;
        s.status = SubscriptionStatus.ACTIVE;
        LocalDateTime now = LocalDateTime.now();
        s.createdAt = now;
        s.updatedAt = now;
        return s;
    }

    /**
     * 取消订阅（软删除）。
     */
    public void cancel() {
        if (this.status == SubscriptionStatus.CANCELLED) {
            throw new IllegalStatusTransitionException("订阅已是取消状态");
        }
        this.status = SubscriptionStatus.CANCELLED;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 重新订阅：复用原记录（受 UNIQUE 约束保护，避免重复数据）。
     */
    public void reactivate() {
        if (this.status == SubscriptionStatus.ACTIVE) {
            throw new IllegalStatusTransitionException("订阅已是生效状态");
        }
        this.status = SubscriptionStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getActivityId() { return activityId; }
    public void setActivityId(Long activityId) { this.activityId = activityId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public SubscriptionStatus getStatus() { return status; }
    public void setStatus(SubscriptionStatus status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
