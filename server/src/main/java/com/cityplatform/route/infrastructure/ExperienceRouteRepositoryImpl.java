package com.cityplatform.route.infrastructure;

import com.cityplatform.route.domain.ExperienceRoute;
import com.cityplatform.route.domain.ExperienceRouteRepository;
import com.cityplatform.route.infrastructure.mapper.ExperienceRouteMapper;
import org.springframework.stereotype.Repository;

/**
 * 路线写模型 Repository 实现。
 */
@Repository
public class ExperienceRouteRepositoryImpl implements ExperienceRouteRepository {

    private final ExperienceRouteMapper mapper;

    public ExperienceRouteRepositoryImpl(ExperienceRouteMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void insert(ExperienceRoute route) {
        mapper.insert(route);
    }

    @Override
    public void update(ExperienceRoute route) {
        mapper.update(route);
    }

    @Override
    public ExperienceRoute findById(Long id) {
        return mapper.selectById(id);
    }
}
