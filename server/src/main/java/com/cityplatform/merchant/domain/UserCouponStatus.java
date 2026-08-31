package com.cityplatform.merchant.domain;

/**
 * 用户优惠券状态。存储态仅 AVAILABLE/REDEEMED；
 * EXPIRED 由读时按券有效期计算，不落库。Demo 阶段不支持取消领取。
 */
public enum UserCouponStatus {
    AVAILABLE,
    REDEEMED
}
