package com.cityplatform.recommendation.application.readmodel;

import java.math.BigDecimal;

/**
 * 街区中心坐标读模型（推荐距离锚点：无用户坐标时使用街区中心，Demo 默认位置策略）。
 */
public class StreetAreaLocationReadModel {

    private Long id;
    private BigDecimal longitude;
    private BigDecimal latitude;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public BigDecimal getLongitude() { return longitude; }
    public void setLongitude(BigDecimal longitude) { this.longitude = longitude; }
    public BigDecimal getLatitude() { return latitude; }
    public void setLatitude(BigDecimal latitude) { this.latitude = latitude; }
}
