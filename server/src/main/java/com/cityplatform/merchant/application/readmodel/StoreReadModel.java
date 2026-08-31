package com.cityplatform.merchant.application.readmodel;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 门店详情查询模型。只读，不进入 Domain。
 * merchantName 通过 JOIN merchant 表获取；coverImageUrl 通过 LEFT JOIN stored_file 获取。
 */
public class StoreReadModel {

    private Long id;
    private Long merchantId;
    private String merchantName;
    private String name;
    private String address;
    private BigDecimal longitude;
    private BigDecimal latitude;
    private String phone;
    private String businessHours;
    private Long coverImageFileId;
    private String coverImageUrl;
    private String businessStatus;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

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
    public BigDecimal getLongitude() { return longitude; }
    public void setLongitude(BigDecimal longitude) { this.longitude = longitude; }
    public BigDecimal getLatitude() { return latitude; }
    public void setLatitude(BigDecimal latitude) { this.latitude = latitude; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getBusinessHours() { return businessHours; }
    public void setBusinessHours(String businessHours) { this.businessHours = businessHours; }
    public Long getCoverImageFileId() { return coverImageFileId; }
    public void setCoverImageFileId(Long coverImageFileId) { this.coverImageFileId = coverImageFileId; }
    public String getCoverImageUrl() { return coverImageUrl; }
    public void setCoverImageUrl(String coverImageUrl) { this.coverImageUrl = coverImageUrl; }
    public String getBusinessStatus() { return businessStatus; }
    public void setBusinessStatus(String businessStatus) { this.businessStatus = businessStatus; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
