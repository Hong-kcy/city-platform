package com.cityplatform.merchant.domain;

/**
 * 用户优惠券写模型 Repository 接口（Domain 层定义，Infrastructure 实现）。
 * markRedeemed 采用条件更新(status=AVAILABLE)兜底并发重复核销。
 */
public interface UserCouponRepository {

    void insert(UserCoupon userCoupon);

    /**
     * 条件核销更新：仅当当前状态为 AVAILABLE 时置为 REDEEMED。
     * 返回 0 表示已被并发核销，由 Application 转为"优惠券已核销"业务错误。
     */
    int markRedeemed(UserCoupon userCoupon);

    UserCoupon findById(Long id);

    UserCoupon findByUserIdAndCouponId(Long userId, Long couponId);

    UserCoupon findByRedeemCode(String redeemCode);
}
