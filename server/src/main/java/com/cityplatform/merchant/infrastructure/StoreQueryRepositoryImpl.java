package com.cityplatform.merchant.infrastructure;

import com.cityplatform.merchant.application.StoreQueryRepository;
import com.cityplatform.merchant.application.dto.StoreQuery;
import com.cityplatform.merchant.application.readmodel.StoreReadModel;
import com.cityplatform.merchant.application.readmodel.StoreSummary;
import com.cityplatform.merchant.infrastructure.mapper.StoreQueryMapper;
import com.cityplatform.platform.web.PageParam;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 门店查询模型 Repository 实现。JOIN merchant 取 merchantName；LEFT JOIN stored_file 取 url。
 */
@Repository
public class StoreQueryRepositoryImpl implements StoreQueryRepository {

    private final StoreQueryMapper mapper;

    public StoreQueryRepositoryImpl(StoreQueryMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public StoreReadModel findById(Long id) {
        return mapper.selectById(id);
    }

    @Override
    public List<StoreSummary> findAll(StoreQuery query, PageParam page) {
        return mapper.selectAll(query, page);
    }

    @Override
    public long count(StoreQuery query) {
        return mapper.count(query);
    }
}
