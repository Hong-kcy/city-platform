package com.cityplatform.merchant.application.dto;

/**
 * 优惠券列表查询条件。
 */
public class CouponQuery {

    private Long storeId;
    private Long merchantId;
    /** 存储状态过滤：ACTIVE/INACTIVE；用户端固定传 ACTIVE */
    private String status;

    public Long getStoreId() { return storeId; }
    public void setStoreId(Long storeId) { this.storeId = storeId; }
    public Long getMerchantId() { return merchantId; }
    public void setMerchantId(Long merchantId) { this.merchantId = merchantId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
