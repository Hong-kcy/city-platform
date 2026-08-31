package com.cityplatform.route.infrastructure.mapper;

import com.cityplatform.route.domain.ExperienceRouteItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 路线 POI 关联写操作 Mapper（单表）。
 */
@Mapper
public interface ExperienceRouteItemMapper {

    int insert(ExperienceRouteItem item);

    int update(ExperienceRouteItem item);

    int deleteById(@Param("id") Long id);

    List<ExperienceRouteItem> selectByRouteIdOrderBySequence(@Param("routeId") Long routeId);

    ExperienceRouteItem selectByRouteIdAndPoiId(@Param("routeId") Long routeId,
                                                @Param("poiId") Long poiId);

    /**
     * 路线下全部关联 sequence 整体加偏移量（批量顺序调整的中间态避让）。
     */
    int shiftSequence(@Param("routeId") Long routeId, @Param("offset") int offset);
}
