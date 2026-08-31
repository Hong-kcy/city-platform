package com.cityplatform.street.infrastructure.mapper;

import com.cityplatform.street.application.readmodel.MapPOIItem;
import com.cityplatform.street.application.readmodel.StreetMapReadModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 街区地图展示 Mapper。
 * 街区信息 + POI 列表分两次查询，由 RepositoryImpl 组装。
 * POI 查询跨域 LEFT JOIN store + stored_file 获取门店展示信息（只读依赖）。
 */
@Mapper
public interface StreetMapQueryMapper {

    StreetMapReadModel selectStreetAreaForMap(@Param("streetAreaId") Long streetAreaId);

    List<MapPOIItem> selectMapPOIs(@Param("streetAreaId") Long streetAreaId);
}
