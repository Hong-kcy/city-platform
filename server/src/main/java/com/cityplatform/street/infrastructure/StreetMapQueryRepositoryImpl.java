package com.cityplatform.street.infrastructure;

import com.cityplatform.street.application.StreetMapQueryRepository;
import com.cityplatform.street.application.readmodel.MapPOIItem;
import com.cityplatform.street.application.readmodel.StreetMapReadModel;
import com.cityplatform.street.infrastructure.mapper.StreetMapQueryMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 街区地图展示 Repository 实现。
 * 两次查询组装：街区信息 + POI 列表。
 * 街区不存在或非 ACTIVE 时返回 null（由 ApplicationService 抛 NotFoundException）。
 */
@Repository
public class StreetMapQueryRepositoryImpl implements StreetMapQueryRepository {

    private final StreetMapQueryMapper mapper;

    public StreetMapQueryRepositoryImpl(StreetMapQueryMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public StreetMapReadModel findMapData(Long streetAreaId) {
        StreetMapReadModel streetMap = mapper.selectStreetAreaForMap(streetAreaId);
        if (streetMap == null) {
            return null;
        }
        List<MapPOIItem> pois = mapper.selectMapPOIs(streetAreaId);
        streetMap.setPois(pois);
        return streetMap;
    }
}
