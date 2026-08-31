package com.cityplatform.street.infrastructure;

import com.cityplatform.street.domain.POI;
import com.cityplatform.street.domain.POIRepository;
import com.cityplatform.street.infrastructure.mapper.POIMapper;
import org.springframework.stereotype.Repository;

/**
 * POI 写模型 Repository 实现。
 */
@Repository
public class POIRepositoryImpl implements POIRepository {

    private final POIMapper mapper;

    public POIRepositoryImpl(POIMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void insert(POI poi) {
        mapper.insert(poi);
    }

    @Override
    public void update(POI poi) {
        mapper.update(poi);
    }

    @Override
    public POI findById(Long id) {
        return mapper.selectById(id);
    }
}
