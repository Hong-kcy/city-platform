package com.cityplatform.merchant.application;

import com.cityplatform.merchant.application.dto.StoreQuery;
import com.cityplatform.merchant.application.readmodel.StoreReadModel;
import com.cityplatform.merchant.application.readmodel.StoreSummary;
import com.cityplatform.platform.web.PageParam;

import java.util.List;

/**
 * 门店查询模型接口（Application 层定义，Infrastructure 实现）。
 * merchantName 通过 JOIN merchant 获取；coverImageUrl 通过 LEFT JOIN stored_file 获取。
 */
public interface StoreQueryRepository {

    StoreReadModel findById(Long id);

    List<StoreSummary> findAll(StoreQuery query, PageParam page);

    long count(StoreQuery query);
}
