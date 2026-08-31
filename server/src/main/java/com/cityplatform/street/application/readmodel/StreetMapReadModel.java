package com.cityplatform.street.application.readmodel;

import java.math.BigDecimal;
import java.util.List;

/**
 * 街区地图展示聚合查询模型。面向用户端地图首页，只读。
 * 包含街区基础信息 + POI 列表（含关联门店展示信息）。
 */
public class StreetMapReadModel {

    private Long id;
    private String name;
    private String introduction;
    private String coverImageUrl;
    private BigDecimal longitude;
    private BigDecimal latitude;
    private List<MapPOIItem> pois;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getIntroduction() { return introduction; }
    public void setIntroduction(String introduction) { this.introduction = introduction; }
    public String getCoverImageUrl() { return coverImageUrl; }
    public void setCoverImageUrl(String coverImageUrl) { this.coverImageUrl = coverImageUrl; }
    public BigDecimal getLongitude() { return longitude; }
    public void setLongitude(BigDecimal longitude) { this.longitude = longitude; }
    public BigDecimal getLatitude() { return latitude; }
    public void setLatitude(BigDecimal latitude) { this.latitude = latitude; }
    public List<MapPOIItem> getPois() { return pois; }
    public void setPois(List<MapPOIItem> pois) { this.pois = pois; }
}
