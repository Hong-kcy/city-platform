package com.cityplatform.merchant.application.readmodel;

import java.time.LocalDateTime;

/**
 * 用户优惠券详情查询模型。核销码仅在本人详情接口暴露（登录态保护）。
 */
public class UserCouponDetailReadModel extends UserCouponSummary {

    /** 核销码：到店出示，商户后台输入核销 */
    private String redeemCode;
    private LocalDateTime redeemedAt;

    public String getRedeemCode() { return redeemCode; }
    public void setRedeemCode(String redeemCode) { this.redeemCode = redeemCode; }
    public LocalDateTime getRedeemedAt() { return redeemedAt; }
    public void setRedeemedAt(LocalDateTime redeemedAt) { this.redeemedAt = redeemedAt; }
}
