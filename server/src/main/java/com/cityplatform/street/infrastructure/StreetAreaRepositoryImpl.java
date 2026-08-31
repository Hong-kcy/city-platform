package com.cityplatform.street.infrastructure;

import com.cityplatform.street.domain.StreetArea;
import com.cityplatform.street.domain.StreetAreaRepository;
import com.cityplatform.street.infrastructure.mapper.StreetAreaMapper;
import org.springframework.stereotype.Repository;

/**
 * 街区写模型 Repository 实现。
 */
@Repository
public class StreetAreaRepositoryImpl implements StreetAreaRepository {

    private final StreetAreaMapper mapper;

    public StreetAreaRepositoryImpl(StreetAreaMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void insert(StreetArea streetArea) {
        mapper.insert(streetArea);
    }

    @Override
    public void update(StreetArea streetArea) {
        mapper.update(streetArea);
    }

    @Override
    public StreetArea findById(Long id) {
        return mapper.selectById(id);
    }
}
