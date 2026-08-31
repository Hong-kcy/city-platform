package com.cityplatform.activity.infrastructure;

import com.cityplatform.activity.application.ActivitySubscriptionQueryRepository;
import com.cityplatform.activity.application.readmodel.ActivitySubscriptionReadModel;
import com.cityplatform.activity.application.readmodel.MyActivitySubscription;
import com.cityplatform.activity.infrastructure.mapper.ActivitySubscriptionQueryMapper;
import com.cityplatform.platform.web.PageParam;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 活动订阅查询模型 Repository 实现。
 */
@Repository
public class ActivitySubscriptionQueryRepositoryImpl implements ActivitySubscriptionQueryRepository {

    private final ActivitySubscriptionQueryMapper mapper;

    public ActivitySubscriptionQueryRepositoryImpl(ActivitySubscriptionQueryMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public ActivitySubscriptionReadModel findByUserIdAndActivityId(Long userId, Long activityId) {
        return mapper.selectByUserIdAndActivityId(userId, activityId);
    }

    @Override
    public List<MyActivitySubscription> findMySubscriptions(Long userId, PageParam page) {
        return mapper.selectMySubscriptions(userId, page);
    }

    @Override
    public long countMySubscriptions(Long userId) {
        return mapper.countMySubscriptions(userId);
    }
}
