package com.cityplatform.street.application;

import com.cityplatform.street.application.readmodel.StreetMapReadModel;

/**
 * 街区地图展示查询接口（Application 层定义，Infrastructure 实现）。
 * 聚合街区信息 + POI 列表（含门店展示信息，跨域 LEFT JOIN store）。
 */
public interface StreetMapQueryRepository {

    StreetMapReadModel findMapData(Long streetAreaId);
}
