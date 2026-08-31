package com.cityplatform.street.infrastructure.mapper;

import com.cityplatform.platform.web.PageParam;
import com.cityplatform.street.application.dto.StreetAreaQuery;
import com.cityplatform.street.application.readmodel.StreetAreaReadModel;
import com.cityplatform.street.application.readmodel.StreetAreaSummary;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 街区查询 Mapper。LEFT JOIN stored_file 取 coverImageUrl。
 */
@Mapper
public interface StreetAreaQueryMapper {

    StreetAreaReadModel selectById(@Param("id") Long id);

    List<StreetAreaSummary> selectAll(@Param("query") StreetAreaQuery query, @Param("page") PageParam page);

    long count(@Param("query") StreetAreaQuery query);
}
