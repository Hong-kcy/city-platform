package com.cityplatform.route.infrastructure;

import com.cityplatform.platform.web.PageParam;
import com.cityplatform.route.application.ExperienceRouteQueryRepository;
import com.cityplatform.route.application.dto.ExperienceRouteQuery;
import com.cityplatform.route.application.readmodel.ExperienceRouteDetailReadModel;
import com.cityplatform.route.application.readmodel.ExperienceRouteSummary;
import com.cityplatform.route.infrastructure.mapper.ExperienceRouteQueryMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 路线查询模型 Repository 实现。面向 ReadModel，不返回 Domain Entity。
 * 详情聚合 = 路线本体单查 + items 单条 JOIN 查询，共 2 条 SQL，无 N+1。
 */
@Repository
public class ExperienceRouteQueryRepositoryImpl implements ExperienceRouteQueryRepository {

    private final ExperienceRouteQueryMapper mapper;

    public ExperienceRouteQueryRepositoryImpl(ExperienceRouteQueryMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public ExperienceRouteSummary findSummaryById(Long id) {
        return mapper.selectSummaryById(id);
    }

    @Override
    public ExperienceRouteDetailReadModel findDetailById(Long id) {
        ExperienceRouteDetailReadModel detail = mapper.selectDetailById(id);
        if (detail != null) {
            detail.setItems(mapper.selectItemsByRouteId(id));
        }
        return detail;
    }

    @Override
    public List<ExperienceRouteSummary> findAll(ExperienceRouteQuery query, PageParam page) {
        return mapper.selectAll(query, page);
    }

    @Override
    public long count(ExperienceRouteQuery query) {
        return mapper.count(query);
    }
}
