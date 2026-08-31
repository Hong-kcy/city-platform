package com.cityplatform.activity.infrastructure.mapper;

import com.cityplatform.activity.application.dto.ActivityQuery;
import com.cityplatform.activity.application.readmodel.ActivityDetailReadModel;
import com.cityplatform.activity.application.readmodel.ActivitySummary;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 活动查询 Mapper。面向 ReadModel，列表/详情通过 JOIN 组装街区名称、
 * POI 摘要与封面 URL。
 */
@Mapper
public interface ActivityQueryMapper {

    ActivitySummary selectById(@Param("id") Long id);

    ActivityDetailReadModel selectDetailById(@Param("id") Long id);

    List<ActivitySummary> selectAll(@Param("query") ActivityQuery query,
                                    @Param("page") com.cityplatform.platform.web.PageParam page);

    long count(@Param("query") ActivityQuery query);
}
