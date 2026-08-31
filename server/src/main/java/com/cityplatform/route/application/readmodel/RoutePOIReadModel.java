package com.cityplatform.route.application.readmodel;

import java.math.BigDecimal;

/**
 * 路线详情中的 POI 项查询模型。按 sequence 排序返回，
 * 附 POI 摘要与 Store 展示信息（查询层 JOIN 组装，前端无需二次请求）。
 * storeName/storeBusinessStatus/storeCoverImageUrl 仅 STORE 类型 POI 有值。
 */
public class RoutePOIReadModel {

    private Integer sequence;
    private Long poiId;
    private String poiName;
    private String poiType;
    private String poiStatus;
    private String poiDescription;
    private BigDecimal longitude;
    private BigDecimal latitude;
    private Long storeId;
    private String storeName;
    private String storeBusinessStatus;
    private String storeCoverImageUrl;
    private String recommendationReason;

    public Integer getSequence() { return sequence; }
    public void setSequence(Integer sequence) { this.sequence = sequence; }
    public Long getPoiId() { return poiId; }
    public void setPoiId(Long poiId) { this.poiId = poiId; }
    public String getPoiName() { return poiName; }
    public void setPoiName(String poiName) { this.poiName = poiName; }
    public String getPoiType() { return poiType; }
    public void setPoiType(String poiType) { this.poiType = poiType; }
    public String getPoiStatus() { return poiStatus; }
    public void setPoiStatus(String poiStatus) { this.poiStatus = poiStatus; }
    public String getPoiDescription() { return poiDescription; }
    public void setPoiDescription(String poiDescription) { this.poiDescription = poiDescription; }
    public BigDecimal getLongitude() { return longitude; }
    public void setLongitude(BigDecimal longitude) { this.longitude = longitude; }
    public BigDecimal getLatitude() { return latitude; }
    public void setLatitude(BigDecimal latitude) { this.latitude = latitude; }
    public Long getStoreId() { return storeId; }
    public void setStoreId(Long storeId) { this.storeId = storeId; }
    public String getStoreName() { return storeName; }
    public void setStoreName(String storeName) { this.storeName = storeName; }
    public String getStoreBusinessStatus() { return storeBusinessStatus; }
    public void setStoreBusinessStatus(String storeBusinessStatus) { this.storeBusinessStatus = storeBusinessStatus; }
    public String getStoreCoverImageUrl() { return storeCoverImageUrl; }
    public void setStoreCoverImageUrl(String storeCoverImageUrl) { this.storeCoverImageUrl = storeCoverImageUrl; }
    public String getRecommendationReason() { return recommendationReason; }
    public void setRecommendationReason(String recommendationReason) { this.recommendationReason = recommendationReason; }
}
