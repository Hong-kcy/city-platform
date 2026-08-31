package com.cityplatform.merchant.application.readmodel;

import java.time.LocalDateTime;

/**
 * "我的优惠券"列表项查询模型。领取信息 + 券摘要（JOIN 组装，避免 N+1）。
 * redeemCode 属敏感业务字段，仅在详情 ReadModel 暴露，列表不返回。
 * effectiveStatus 为读时计算：AVAILABLE/REDEEMED/EXPIRED。
 */
public class UserCouponSummary {

    private Long id;
    private Long couponId;
    private String name;
    private String discountText;
    private String description;
    private String storeName;
    private String merchantName;
    private LocalDateTime validFrom;
    private LocalDateTime validTo;
    private String status;
    private String effectiveStatus;
    private LocalDateTime claimedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCouponId() { return couponId; }
    public void setCouponId(Long couponId) { this.couponId = couponId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDiscountText() { return discountText; }
    public void setDiscountText(String discountText) { this.discountText = discountText; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getStoreName() { return storeName; }
    public void setStoreName(String storeName) { this.storeName = storeName; }
    public String getMerchantName() { return merchantName; }
    public void setMerchantName(String merchantName) { this.merchantName = merchantName; }
    public LocalDateTime getValidFrom() { return validFrom; }
    public void setValidFrom(LocalDateTime validFrom) { this.validFrom = validFrom; }
    public LocalDateTime getValidTo() { return validTo; }
    public void setValidTo(LocalDateTime validTo) { this.validTo = validTo; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getEffectiveStatus() { return effectiveStatus; }
    public void setEffectiveStatus(String effectiveStatus) { this.effectiveStatus = effectiveStatus; }
    public LocalDateTime getClaimedAt() { return claimedAt; }
    public void setClaimedAt(LocalDateTime claimedAt) { this.claimedAt = claimedAt; }
}
