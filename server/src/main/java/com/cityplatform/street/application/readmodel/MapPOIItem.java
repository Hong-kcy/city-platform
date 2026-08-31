package com.cityplatform.street.application.readmodel;

import java.math.BigDecimal;

/**
 * 地图 POI 展示项。属于 StreetMapReadModel 的子模型。
 * 门店展示信息通过 LEFT JOIN store + stored_file 获取，可空。
 */
public class MapPOIItem {

    private Long id;
    private String name;
    private String poiType;
    private BigDecimal longitude;
    private BigDecimal latitude;
    private String description;
    private Long storeId;
    private String storeName;
    private String storeBusinessStatus;
    private String storeCoverImageUrl;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPoiType() { return poiType; }
    public void setPoiType(String poiType) { this.poiType = poiType; }
    public BigDecimal getLongitude() { return longitude; }
    public void setLongitude(BigDecimal longitude) { this.longitude = longitude; }
    public BigDecimal getLatitude() { return latitude; }
    public void setLatitude(BigDecimal latitude) { this.latitude = latitude; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Long getStoreId() { return storeId; }
    public void setStoreId(Long storeId) { this.storeId = storeId; }
    public String getStoreName() { return storeName; }
    public void setStoreName(String storeName) { this.storeName = storeName; }
    public String getStoreBusinessStatus() { return storeBusinessStatus; }
    public void setStoreBusinessStatus(String storeBusinessStatus) { this.storeBusinessStatus = storeBusinessStatus; }
    public String getStoreCoverImageUrl() { return storeCoverImageUrl; }
    public void setStoreCoverImageUrl(String storeCoverImageUrl) { this.storeCoverImageUrl = storeCoverImageUrl; }
}
