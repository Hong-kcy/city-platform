package com.cityplatform.activity.application;

import com.cityplatform.activity.application.dto.ActivityQuery;
import com.cityplatform.activity.application.readmodel.ActivityDetailReadModel;
import com.cityplatform.activity.application.readmodel.ActivitySummary;
import com.cityplatform.platform.web.PageParam;

import java.util.List;

/**
 * 活动查询模型接口（Application 层定义，Infrastructure 实现）。
 * 详情聚合（街区名称/POI 摘要/封面 URL）通过 SQL JOIN 完成。
 */
public interface ActivityQueryRepository {

    ActivitySummary findById(Long id);

    ActivityDetailReadModel findDetailById(Long id);

    List<ActivitySummary> findAll(ActivityQuery query, PageParam page);

    long count(ActivityQuery query);
}
