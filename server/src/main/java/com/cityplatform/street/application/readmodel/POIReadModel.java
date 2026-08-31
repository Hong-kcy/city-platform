package com.cityplatform.street.application.readmodel;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * POI 详情查询模型。只读，不进入 Domain。
 * streetAreaName 通过 JOIN street_area 表获取。
 * poiType=STORE 时聚合 Store 展示摘要（storeName/businessStatus/coverImageUrl），
 * 点击 POI 后前端无需二次请求 Store 详情；非 STORE POI 这些字段为 null。
 */
public class POIReadModel {

    private Long id;
    private Long streetAreaId;
    private String streetAreaName;
    private String name;
    private String poiType;
    private Long storeId;
    private BigDecimal longitude;
    private BigDecimal latitude;
    private String description;
    private String status;
    private String storeName;
    private String storeBusinessStatus;
    private String storeCoverImageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getStreetAreaId() { return streetAreaId; }
    public void setStreetAreaId(Long streetAreaId) { this.streetAreaId = streetAreaId; }
    public String getStreetAreaName() { return streetAreaName; }
    public void setStreetAreaName(String streetAreaName) { this.streetAreaName = streetAreaName; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPoiType() { return poiType; }
    public void setPoiType(String poiType) { this.poiType = poiType; }
    public Long getStoreId() { return storeId; }
    public void setStoreId(Long storeId) { this.storeId = storeId; }
    public BigDecimal getLongitude() { return longitude; }
    public void setLongitude(BigDecimal longitude) { this.longitude = longitude; }
    public BigDecimal getLatitude() { return latitude; }
    public void setLatitude(BigDecimal latitude) { this.latitude = latitude; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getStoreName() { return storeName; }
    public void setStoreName(String storeName) { this.storeName = storeName; }
    public String getStoreBusinessStatus() { return storeBusinessStatus; }
    public void setStoreBusinessStatus(String storeBusinessStatus) { this.storeBusinessStatus = storeBusinessStatus; }
    public String getStoreCoverImageUrl() { return storeCoverImageUrl; }
    public void setStoreCoverImageUrl(String storeCoverImageUrl) { this.storeCoverImageUrl = storeCoverImageUrl; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
