package com.cityplatform.activity.infrastructure.mapper;

import com.cityplatform.activity.application.readmodel.ActivitySubscriptionReadModel;
import com.cityplatform.activity.application.readmodel.MyActivitySubscription;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 活动订阅查询 Mapper。"我的活动"通过 JOIN activity/street_area/stored_file 组装。
 */
@Mapper
public interface ActivitySubscriptionQueryMapper {

    ActivitySubscriptionReadModel selectByUserIdAndActivityId(@Param("userId") Long userId,
                                                              @Param("activityId") Long activityId);

    List<MyActivitySubscription> selectMySubscriptions(@Param("userId") Long userId,
                                                       @Param("page") com.cityplatform.platform.web.PageParam page);

    long countMySubscriptions(@Param("userId") Long userId);
}
