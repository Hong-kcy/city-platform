package com.cityplatform.street.infrastructure;

import com.cityplatform.platform.web.PageParam;
import com.cityplatform.street.application.POIQueryRepository;
import com.cityplatform.street.application.dto.POIQuery;
import com.cityplatform.street.application.readmodel.POIReadModel;
import com.cityplatform.street.application.readmodel.POISummary;
import com.cityplatform.street.infrastructure.mapper.POIQueryMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * POI 查询模型 Repository 实现。JOIN street_area 取 streetAreaName。
 */
@Repository
public class POIQueryRepositoryImpl implements POIQueryRepository {

    private final POIQueryMapper mapper;

    public POIQueryRepositoryImpl(POIQueryMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public POIReadModel findById(Long id) {
        return mapper.selectById(id);
    }

    @Override
    public List<POISummary> findAll(POIQuery query, PageParam page) {
        return mapper.selectAll(query, page);
    }

    @Override
    public long count(POIQuery query) {
        return mapper.count(query);
    }
}
