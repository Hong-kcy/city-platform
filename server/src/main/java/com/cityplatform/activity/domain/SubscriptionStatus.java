package com.cityplatform.activity.domain;

/**
 * 活动订阅状态。取消订阅为软删除，保留记录用于重新订阅与统计。
 */
public enum SubscriptionStatus {
    ACTIVE,
    CANCELLED
}
