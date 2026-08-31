package com.cityplatform.merchant.application.readmodel;

import java.time.LocalDateTime;

/**
 * 商户核销结果查询模型。
 */
public class RedeemResultReadModel {

    private String redeemCode;
    private Long userCouponId;
    private String couponName;
    private String discountText;
    private String storeName;
    private LocalDateTime redeemedAt;

    public RedeemResultReadModel() {
    }

    public RedeemResultReadModel(String redeemCode, Long userCouponId, String couponName,
                                  String discountText, String storeName, LocalDateTime redeemedAt) {
        this.redeemCode = redeemCode;
        this.userCouponId = userCouponId;
        this.couponName = couponName;
        this.discountText = discountText;
        this.storeName = storeName;
        this.redeemedAt = redeemedAt;
    }

    public String getRedeemCode() { return redeemCode; }
    public void setRedeemCode(String redeemCode) { this.redeemCode = redeemCode; }
    public Long getUserCouponId() { return userCouponId; }
    public void setUserCouponId(Long userCouponId) { this.userCouponId = userCouponId; }
    public String getCouponName() { return couponName; }
    public void setCouponName(String couponName) { this.couponName = couponName; }
    public String getDiscountText() { return discountText; }
    public void setDiscountText(String discountText) { this.discountText = discountText; }
    public String getStoreName() { return storeName; }
    public void setStoreName(String storeName) { this.storeName = storeName; }
    public LocalDateTime getRedeemedAt() { return redeemedAt; }
    public void setRedeemedAt(LocalDateTime redeemedAt) { this.redeemedAt = redeemedAt; }
}
