package com.cityplatform.street.application;

import com.cityplatform.platform.web.PageParam;
import com.cityplatform.street.application.dto.POIQuery;
import com.cityplatform.street.application.readmodel.POIReadModel;
import com.cityplatform.street.application.readmodel.POISummary;

import java.util.List;

/**
 * POI 查询模型接口（Application 层定义，Infrastructure 实现）。
 * streetAreaName 通过 JOIN street_area 获取。
 */
public interface POIQueryRepository {

    POIReadModel findById(Long id);

    List<POISummary> findAll(POIQuery query, PageParam page);

    long count(POIQuery query);
}
