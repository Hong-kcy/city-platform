package com.cityplatform.merchant.application.readmodel;

/**
 * 门店列表项查询模型。只读，精简字段。
 */
public class StoreSummary {

    private Long id;
    private Long merchantId;
    private String merchantName;
    private String name;
    private String address;
    private String businessStatus;
    private String status;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getMerchantId() { return merchantId; }
    public void setMerchantId(Long merchantId) { this.merchantId = merchantId; }
    public String getMerchantName() { return merchantName; }
    public void setMerchantName(String merchantName) { this.merchantName = merchantName; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getBusinessStatus() { return businessStatus; }
    public void setBusinessStatus(String businessStatus) { this.businessStatus = businessStatus; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
