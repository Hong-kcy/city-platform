package com.cityplatform.merchant.application.dto;

/**
 * 门店列表查询条件。
 */
public class StoreQuery {

    private Long merchantId;
    private String businessStatus;
    private String status;

    public Long getMerchantId() { return merchantId; }
    public void setMerchantId(Long merchantId) { this.merchantId = merchantId; }
    public String getBusinessStatus() { return businessStatus; }
    public void setBusinessStatus(String businessStatus) { this.businessStatus = businessStatus; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
