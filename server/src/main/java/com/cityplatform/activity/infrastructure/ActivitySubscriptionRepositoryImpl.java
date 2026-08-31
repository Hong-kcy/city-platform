package com.cityplatform.activity.infrastructure;

import com.cityplatform.activity.domain.ActivitySubscription;
import com.cityplatform.activity.domain.ActivitySubscriptionRepository;
import com.cityplatform.activity.infrastructure.mapper.ActivitySubscriptionMapper;
import org.springframework.stereotype.Repository;

/**
 * 活动订阅写模型 Repository 实现。
 */
@Repository
public class ActivitySubscriptionRepositoryImpl implements ActivitySubscriptionRepository {

    private final ActivitySubscriptionMapper mapper;

    public ActivitySubscriptionRepositoryImpl(ActivitySubscriptionMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void insert(ActivitySubscription subscription) {
        mapper.insert(subscription);
    }

    @Override
    public void update(ActivitySubscription subscription) {
        mapper.update(subscription);
    }

    @Override
    public ActivitySubscription findByUserIdAndActivityId(Long userId, Long activityId) {
        return mapper.selectByUserIdAndActivityId(userId, activityId);
    }
}
