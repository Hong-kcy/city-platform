package com.cityplatform.activity.application;

import com.cityplatform.activity.application.readmodel.ActivitySubscriptionReadModel;
import com.cityplatform.activity.application.readmodel.MyActivitySubscription;
import com.cityplatform.platform.web.PageParam;

import java.util.List;

/**
 * 活动订阅查询模型接口（Application 层定义，Infrastructure 实现）。
 * "我的活动"列表通过 JOIN activity/street_area/stored_file 组装，避免 N+1。
 */
public interface ActivitySubscriptionQueryRepository {

    ActivitySubscriptionReadModel findByUserIdAndActivityId(Long userId, Long activityId);

    List<MyActivitySubscription> findMySubscriptions(Long userId, PageParam page);

    long countMySubscriptions(Long userId);
}
