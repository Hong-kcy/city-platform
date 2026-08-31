package com.cityplatform.route.infrastructure;

import com.cityplatform.route.domain.ExperienceRouteItem;
import com.cityplatform.route.domain.ExperienceRouteItemRepository;
import com.cityplatform.route.infrastructure.mapper.ExperienceRouteItemMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 路线 POI 关联写模型 Repository 实现。
 */
@Repository
public class ExperienceRouteItemRepositoryImpl implements ExperienceRouteItemRepository {

    private final ExperienceRouteItemMapper mapper;

    public ExperienceRouteItemRepositoryImpl(ExperienceRouteItemMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void insert(ExperienceRouteItem item) {
        mapper.insert(item);
    }

    @Override
    public void update(ExperienceRouteItem item) {
        mapper.update(item);
    }

    @Override
    public void deleteById(Long id) {
        mapper.deleteById(id);
    }

    @Override
    public List<ExperienceRouteItem> findByRouteIdOrderBySequence(Long routeId) {
        return mapper.selectByRouteIdOrderBySequence(routeId);
    }

    @Override
    public ExperienceRouteItem findByRouteIdAndPoiId(Long routeId, Long poiId) {
        return mapper.selectByRouteIdAndPoiId(routeId, poiId);
    }

    @Override
    public void shiftSequence(Long routeId, int offset) {
        mapper.shiftSequence(routeId, offset);
    }
}
