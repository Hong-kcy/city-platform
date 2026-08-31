package com.cityplatform.activity.infrastructure.mapper;

import com.cityplatform.activity.domain.ActivitySubscription;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 活动订阅写操作 Mapper（单表）。
 */
@Mapper
public interface ActivitySubscriptionMapper {

    int insert(ActivitySubscription subscription);

    int update(ActivitySubscription subscription);

    ActivitySubscription selectByUserIdAndActivityId(@Param("userId") Long userId,
                                                     @Param("activityId") Long activityId);
}
