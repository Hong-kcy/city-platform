package com.cityplatform.activity.infrastructure;

import com.cityplatform.activity.domain.Activity;
import com.cityplatform.activity.domain.ActivityRepository;
import com.cityplatform.activity.infrastructure.mapper.ActivityMapper;
import org.springframework.stereotype.Repository;

/**
 * 活动写模型 Repository 实现。实现 Domain 层接口，调用 Mapper。
 */
@Repository
public class ActivityRepositoryImpl implements ActivityRepository {

    private final ActivityMapper mapper;

    public ActivityRepositoryImpl(ActivityMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void insert(Activity activity) {
        mapper.insert(activity);
    }

    @Override
    public void update(Activity activity) {
        mapper.update(activity);
    }

    @Override
    public Activity findById(Long id) {
        return mapper.selectById(id);
    }
}
