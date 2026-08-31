package com.cityplatform.street.application.readmodel;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 街区详情查询模型。只读，不进入 Domain。
 * coverImageUrl 通过 LEFT JOIN stored_file 获取，COALESCE 兜底默认图。
 */
public class StreetAreaReadModel {

    private Long id;
    private String name;
    private String introduction;
    private Long coverImageFileId;
    private String coverImageUrl;
    private BigDecimal longitude;
    private BigDecimal latitude;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getIntroduction() { return introduction; }
    public void setIntroduction(String introduction) { this.introduction = introduction; }
    public Long getCoverImageFileId() { return coverImageFileId; }
    public void setCoverImageFileId(Long coverImageFileId) { this.coverImageFileId = coverImageFileId; }
    public String getCoverImageUrl() { return coverImageUrl; }
    public void setCoverImageUrl(String coverImageUrl) { this.coverImageUrl = coverImageUrl; }
    public BigDecimal getLongitude() { return longitude; }
    public void setLongitude(BigDecimal longitude) { this.longitude = longitude; }
    public BigDecimal getLatitude() { return latitude; }
    public void setLatitude(BigDecimal latitude) { this.latitude = latitude; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
