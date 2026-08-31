package com.cityplatform.merchant.application.readmodel;

/**
 * 优惠券详情查询模型。携带有效登录态时附带当前用户领取状态（claimed）。
 */
public class CouponDetailReadModel extends CouponSummary {

    /** 当前用户是否已领取（匿名访问为 null） */
    private Boolean claimed;

    public Boolean getClaimed() { return claimed; }
    public void setClaimed(Boolean claimed) { this.claimed = claimed; }
}
