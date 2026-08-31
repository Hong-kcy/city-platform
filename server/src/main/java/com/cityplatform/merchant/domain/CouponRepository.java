package com.cityplatform.merchant.domain;

/**
 * 优惠券写模型 Repository 接口（Domain 层定义，Infrastructure 实现）。
 */
public interface CouponRepository {

    void insert(Coupon coupon);

    void update(Coupon coupon);

    Coupon findById(Long id);
}
