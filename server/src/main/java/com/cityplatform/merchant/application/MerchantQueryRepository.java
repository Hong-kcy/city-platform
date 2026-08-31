package com.cityplatform.merchant.application;

import com.cityplatform.merchant.application.dto.MerchantQuery;
import com.cityplatform.merchant.application.readmodel.MerchantReadModel;
import com.cityplatform.merchant.application.readmodel.MerchantSummary;
import com.cityplatform.platform.web.PageParam;

import java.util.List;

/**
 * 商户查询模型接口（Application 层定义，Infrastructure 实现）。
 * 面向 ReadModel，可用 SQL JOIN 组装聚合视图，避免 N+1。
 * ReadModel 不进入 Domain。
 */
public interface MerchantQueryRepository {

    MerchantReadModel findById(Long id);

    List<MerchantSummary> findAll(MerchantQuery query, PageParam page);

    long count(MerchantQuery query);
}
