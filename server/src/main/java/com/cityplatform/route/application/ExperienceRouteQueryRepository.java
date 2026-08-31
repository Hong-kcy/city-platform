package com.cityplatform.route.application;

import com.cityplatform.route.application.dto.ExperienceRouteQuery;
import com.cityplatform.route.application.readmodel.ExperienceRouteDetailReadModel;
import com.cityplatform.route.application.readmodel.ExperienceRouteSummary;
import com.cityplatform.platform.web.PageParam;

import java.util.List;

/**
 * 路线查询模型接口（Application 层定义，Infrastructure 实现）。
 * 详情聚合（街区名称/POI 列表/Store 展示信息）通过 SQL JOIN 完成。
 */
public interface ExperienceRouteQueryRepository {

    ExperienceRouteSummary findSummaryById(Long id);

    ExperienceRouteDetailReadModel findDetailById(Long id);

    List<ExperienceRouteSummary> findAll(ExperienceRouteQuery query, PageParam page);

    long count(ExperienceRouteQuery query);
}
