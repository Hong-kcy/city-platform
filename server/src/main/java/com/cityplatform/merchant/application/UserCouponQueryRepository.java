package com.cityplatform.merchant.application;

import com.cityplatform.merchant.application.readmodel.UserCouponDetailReadModel;
import com.cityplatform.merchant.application.readmodel.UserCouponSummary;
import com.cityplatform.platform.web.PageParam;

import java.util.List;

/**
 * 用户优惠券读模型查询接口（Application 层定义，Infrastructure 用 SQL JOIN 实现）。
 */
public interface UserCouponQueryRepository {

    UserCouponSummary findByUserIdAndCouponId(Long userId, Long couponId);

    List<UserCouponSummary> findMyCoupons(Long userId, PageParam page);

    long countMyCoupons(Long userId);

    UserCouponDetailReadModel findMyCouponDetail(Long userId, Long userCouponId);
}
