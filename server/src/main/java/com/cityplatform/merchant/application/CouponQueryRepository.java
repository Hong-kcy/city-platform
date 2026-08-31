package com.cityplatform.merchant.application;

import com.cityplatform.merchant.application.dto.CouponQuery;
import com.cityplatform.merchant.application.readmodel.CouponDetailReadModel;
import com.cityplatform.merchant.application.readmodel.CouponSummary;
import com.cityplatform.platform.web.PageParam;

import java.util.List;

/**
 * 优惠券读模型查询接口（Application 层定义，Infrastructure 用 SQL JOIN 实现）。
 */
public interface CouponQueryRepository {

    CouponSummary findById(Long id);

    CouponDetailReadModel findDetailById(Long id);

    List<CouponSummary> findAll(CouponQuery query, PageParam page);

    long count(CouponQuery query);
}
