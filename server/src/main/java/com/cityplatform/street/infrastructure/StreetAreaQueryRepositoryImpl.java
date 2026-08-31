package com.cityplatform.street.infrastructure;

import com.cityplatform.platform.web.PageParam;
import com.cityplatform.street.application.StreetAreaQueryRepository;
import com.cityplatform.street.application.dto.StreetAreaQuery;
import com.cityplatform.street.application.readmodel.StreetAreaReadModel;
import com.cityplatform.street.application.readmodel.StreetAreaSummary;
import com.cityplatform.street.infrastructure.mapper.StreetAreaQueryMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 街区查询模型 Repository 实现。LEFT JOIN stored_file 取 coverImageUrl。
 */
@Repository
public class StreetAreaQueryRepositoryImpl implements StreetAreaQueryRepository {

    private final StreetAreaQueryMapper mapper;

    public StreetAreaQueryRepositoryImpl(StreetAreaQueryMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public StreetAreaReadModel findById(Long id) {
        return mapper.selectById(id);
    }

    @Override
    public List<StreetAreaSummary> findAll(StreetAreaQuery query, PageParam page) {
        return mapper.selectAll(query, page);
    }

    @Override
    public long count(StreetAreaQuery query) {
        return mapper.count(query);
    }
}
