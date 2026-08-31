package com.cityplatform.merchant.domain;

/**
 * 优惠券状态。仅维护存储态 ACTIVE/INACTIVE；
 * EXPIRED/NOT_STARTED 由读时按有效期计算，不落库。
 */
public enum CouponStatus {
    ACTIVE,
    INACTIVE
}
