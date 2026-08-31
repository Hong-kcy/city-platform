package com.cityplatform.activity.infrastructure;

import com.cityplatform.activity.application.ActivityQueryRepository;
import com.cityplatform.activity.application.dto.ActivityQuery;
import com.cityplatform.activity.application.readmodel.ActivityDetailReadModel;
import com.cityplatform.activity.application.readmodel.ActivitySummary;
import com.cityplatform.activity.infrastructure.mapper.ActivityQueryMapper;
import com.cityplatform.platform.web.PageParam;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 活动查询模型 Repository 实现。面向 ReadModel，不返回 Domain Entity。
 */
@Repository
public class ActivityQueryRepositoryImpl implements ActivityQueryRepository {

    private final ActivityQueryMapper mapper;

    public ActivityQueryRepositoryImpl(ActivityQueryMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public ActivitySummary findById(Long id) {
        return mapper.selectById(id);
    }

    @Override
    public ActivityDetailReadModel findDetailById(Long id) {
        return mapper.selectDetailById(id);
    }

    @Override
    public List<ActivitySummary> findAll(ActivityQuery query, PageParam page) {
        return mapper.selectAll(query, page);
    }

    @Override
    public long count(ActivityQuery query) {
        return mapper.count(query);
    }
}
