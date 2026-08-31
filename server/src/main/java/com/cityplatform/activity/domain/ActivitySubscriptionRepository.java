package com.cityplatform.activity.domain;

/**
 * 活动订阅写模型 Repository 接口（Domain 层定义，Infrastructure 实现）。
 */
public interface ActivitySubscriptionRepository {

    void insert(ActivitySubscription subscription);

    void update(ActivitySubscription subscription);

    ActivitySubscription findByUserIdAndActivityId(Long userId, Long activityId);
}
