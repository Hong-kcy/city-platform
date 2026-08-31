package com.cityplatform.merchant.application.readmodel;

import java.time.LocalDateTime;

/**
 * 优惠券摘要查询模型（列表用）。跨域字段（门店名/商户名）由 SQL JOIN 组装。
 * effectiveStatus 为读时计算值：ACTIVE/INACTIVE/NOT_STARTED/EXPIRED。
 */
public class CouponSummary {

    private Long id;
    private Long merchantId;
    private Long storeId;
    private String storeName;
    private String merchantName;
    private String name;
    private String description;
    private String discountText;
    private LocalDateTime validFrom;
    private LocalDateTime validTo;
    private String status;
    private String effectiveStatus;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getMerchantId() { return merchantId; }
    public void setMerchantId(Long merchantId) { this.merchantId = merchantId; }
    public Long getStoreId() { return storeId; }
    public void setStoreId(Long storeId) { this.storeId = storeId; }
    public String getStoreName() { return storeName; }
    public void setStoreName(String storeName) { this.storeName = storeName; }
    public String getMerchantName() { return merchantName; }
    public void setMerchantName(String merchantName) { this.merchantName = merchantName; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getDiscountText() { return discountText; }
    public void setDiscountText(String discountText) { this.discountText = discountText; }
    public LocalDateTime getValidFrom() { return validFrom; }
    public void setValidFrom(LocalDateTime validFrom) { this.validFrom = validFrom; }
    public LocalDateTime getValidTo() { return validTo; }
    public void setValidTo(LocalDateTime validTo) { this.validTo = validTo; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getEffectiveStatus() { return effectiveStatus; }
    public void setEffectiveStatus(String effectiveStatus) { this.effectiveStatus = effectiveStatus; }
}
