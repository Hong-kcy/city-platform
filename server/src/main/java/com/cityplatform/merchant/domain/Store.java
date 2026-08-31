package com.cityplatform.merchant.domain;

import com.cityplatform.platform.exception.IllegalStatusTransitionException;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 门店 Entity。纯 POJO。
 * address/longitude/latitude 属于工商运营数据，不引用 Street 域 POI。
 */
public class Store {

    private Long id;
    private Long merchantId;
    private String name;
    private String address;
    private BigDecimal longitude;
    private BigDecimal latitude;
    private String phone;
    private String businessHours;
    private Long coverImageFileId;
    private BusinessStatus businessStatus;
    private StoreStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * 工厂方法：创建门店。默认 OPEN / ACTIVE。
     */
    public static Store create(Long merchantId, String name, String address,
                               BigDecimal longitude, BigDecimal latitude, String phone,
                               String businessHours, Long coverImageFileId) {
        Store s = new Store();
        s.merchantId = merchantId;
        s.name = name;
        s.address = address;
        s.longitude = longitude;
        s.latitude = latitude;
        s.phone = phone;
        s.businessHours = businessHours;
        s.coverImageFileId = coverImageFileId;
        s.businessStatus = BusinessStatus.OPEN;
        s.status = StoreStatus.ACTIVE;
        LocalDateTime now = LocalDateTime.now();
        s.createdAt = now;
        s.updatedAt = now;
        return s;
    }

    /**
     * 修改基础信息（PUT 全量替换语义）。
     */
    public void updateInfo(String name, String address, BigDecimal longitude, BigDecimal latitude,
                           String phone, String businessHours, Long coverImageFileId) {
        this.name = name;
        this.address = address;
        this.longitude = longitude;
        this.latitude = latitude;
        this.phone = phone;
        this.businessHours = businessHours;
        this.coverImageFileId = coverImageFileId;
        this.updatedAt = LocalDateTime.now();
    }

    public void changeBusinessStatus(BusinessStatus target) {
        if (this.businessStatus == target) {
            throw new IllegalStatusTransitionException("门店营业状态已是: " + target);
        }
        this.businessStatus = target;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        if (this.status == StoreStatus.ACTIVE) {
            throw new IllegalStatusTransitionException("门店已是启用状态");
        }
        this.status = StoreStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

    public void deactivate() {
        if (this.status == StoreStatus.INACTIVE) {
            throw new IllegalStatusTransitionException("门店已是停用状态");
        }
        this.status = StoreStatus.INACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getMerchantId() { return merchantId; }
    public void setMerchantId(Long merchantId) { this.merchantId = merchantId; }
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
    public BusinessStatus getBusinessStatus() { return businessStatus; }
    public void setBusinessStatus(BusinessStatus businessStatus) { this.businessStatus = businessStatus; }
    public StoreStatus getStatus() { return status; }
    public void setStatus(StoreStatus status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
