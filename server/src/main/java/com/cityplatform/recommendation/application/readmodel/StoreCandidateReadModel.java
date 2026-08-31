package com.cityplatform.recommendation.application.readmodel;

import java.math.BigDecimal;

/**
 * 门店候选读模型（跨域聚合 SQL 投影，仅供推荐候选准备使用）。
 */
public class StoreCandidateReadModel {

    private Long id;
    private String name;
    private String merchantType;
    private BigDecimal longitude;
    private BigDecimal latitude;
    private String coverImageUrl;
    private Boolean hasCoupon;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getMerchantType() { return merchantType; }
    public void setMerchantType(String merchantType) { this.merchantType = merchantType; }
    public BigDecimal getLongitude() { return longitude; }
    public void setLongitude(BigDecimal longitude) { this.longitude = longitude; }
    public BigDecimal getLatitude() { return latitude; }
    public void setLatitude(BigDecimal latitude) { this.latitude = latitude; }
    public String getCoverImageUrl() { return coverImageUrl; }
    public void setCoverImageUrl(String coverImageUrl) { this.coverImageUrl = coverImageUrl; }
    public Boolean getHasCoupon() { return hasCoupon; }
    public void setHasCoupon(Boolean hasCoupon) { this.hasCoupon = hasCoupon; }
}
