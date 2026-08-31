package com.cityplatform.street.infrastructure.mapper;

import com.cityplatform.platform.web.PageParam;
import com.cityplatform.street.application.dto.POIQuery;
import com.cityplatform.street.application.readmodel.POIReadModel;
import com.cityplatform.street.application.readmodel.POISummary;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * POI 查询 Mapper。JOIN street_area 取 streetAreaName。
 */
@Mapper
public interface POIQueryMapper {

    POIReadModel selectById(@Param("id") Long id);

    List<POISummary> selectAll(@Param("query") POIQuery query, @Param("page") PageParam page);

    long count(@Param("query") POIQuery query);
}
