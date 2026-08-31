package com.cityplatform.merchant.infrastructure;

import com.cityplatform.merchant.domain.Store;
import com.cityplatform.merchant.domain.StoreRepository;
import com.cityplatform.merchant.infrastructure.mapper.StoreMapper;
import org.springframework.stereotype.Repository;

/**
 * 门店写模型 Repository 实现。
 */
@Repository
public class StoreRepositoryImpl implements StoreRepository {

    private final StoreMapper mapper;

    public StoreRepositoryImpl(StoreMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void insert(Store store) {
        mapper.insert(store);
    }

    @Override
    public void update(Store store) {
        mapper.update(store);
    }

    @Override
    public Store findById(Long id) {
        return mapper.selectById(id);
    }
}
