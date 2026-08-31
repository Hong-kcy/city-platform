package com.cityplatform.route.infrastructure.mapper;

import com.cityplatform.platform.web.PageParam;
import com.cityplatform.route.application.dto.ExperienceRouteQuery;
import com.cityplatform.route.application.readmodel.ExperienceRouteDetailReadModel;
import com.cityplatform.route.application.readmodel.ExperienceRouteSummary;
import com.cityplatform.route.application.readmodel.RoutePOIReadModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 路线查询 Mapper。面向 ReadModel，列表/详情通过 JOIN 组装街区名称、
 * POI 摘要与 Store 展示信息。详情 items 为独立单条 JOIN 查询，无 N+1。
 */
@Mapper
public interface ExperienceRouteQueryMapper {

    ExperienceRouteSummary selectSummaryById(@Param("id") Long id);

    ExperienceRouteDetailReadModel selectDetailById(@Param("id") Long id);

    List<RoutePOIReadModel> selectItemsByRouteId(@Param("routeId") Long routeId);

    List<ExperienceRouteSummary> selectAll(@Param("query") ExperienceRouteQuery query,
                                           @Param("page") PageParam page);

    long count(@Param("query") ExperienceRouteQuery query);
}
