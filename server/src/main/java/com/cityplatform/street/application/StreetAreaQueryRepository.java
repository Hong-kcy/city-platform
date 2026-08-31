package com.cityplatform.street.application;

import com.cityplatform.platform.web.PageParam;
import com.cityplatform.street.application.dto.StreetAreaQuery;
import com.cityplatform.street.application.readmodel.StreetAreaReadModel;
import com.cityplatform.street.application.readmodel.StreetAreaSummary;

import java.util.List;

/**
 * 街区查询模型接口（Application 层定义，Infrastructure 实现）。
 * coverImageUrl 通过 LEFT JOIN stored_file 获取。
 */
public interface StreetAreaQueryRepository {

    StreetAreaReadModel findById(Long id);

    List<StreetAreaSummary> findAll(StreetAreaQuery query, PageParam page);

    long count(StreetAreaQuery query);
}
