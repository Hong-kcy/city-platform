package com.cityplatform.street.application.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class UpdateStreetAreaRequest {

    @NotBlank(message = "街区名称不能为空")
    @Size(max = 100, message = "街区名称最长100字符")
    private String name;

    @Size(max = 500, message = "街区简介最长500字符")
    private String introduction;

    private Long coverImageFileId;

    @NotNull(message = "经度不能为空")
    @DecimalMin(value = "-180.0000000", message = "经度非法")
    @DecimalMax(value = "180.0000000", message = "经度非法")
    private BigDecimal longitude;

    @NotNull(message = "纬度不能为空")
    @DecimalMin(value = "-90.0000000", message = "纬度非法")
    @DecimalMax(value = "90.0000000", message = "纬度非法")
    private BigDecimal latitude;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getIntroduction() { return introduction; }
    public void setIntroduction(String introduction) { this.introduction = introduction; }
    public Long getCoverImageFileId() { return coverImageFileId; }
    public void setCoverImageFileId(Long coverImageFileId) { this.coverImageFileId = coverImageFileId; }
    public BigDecimal getLongitude() { return longitude; }
    public void setLongitude(BigDecimal longitude) { this.longitude = longitude; }
    public BigDecimal getLatitude() { return latitude; }
    public void setLatitude(BigDecimal latitude) { this.latitude = latitude; }
}
