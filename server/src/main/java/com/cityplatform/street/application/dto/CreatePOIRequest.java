package com.cityplatform.street.application.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class CreatePOIRequest {

    @NotBlank(message = "POI名称不能为空")
    @Size(max = 100, message = "POI名称最长100字符")
    private String name;

    @NotBlank(message = "POI类型不能为空")
    private String poiType;

    private Long storeId;

    @NotNull(message = "经度不能为空")
    @DecimalMin(value = "-180.0000000", message = "经度非法")
    @DecimalMax(value = "180.0000000", message = "经度非法")
    private BigDecimal longitude;

    @NotNull(message = "纬度不能为空")
    @DecimalMin(value = "-90.0000000", message = "纬度非法")
    @DecimalMax(value = "90.0000000", message = "纬度非法")
    private BigDecimal latitude;

    @Size(max = 500, message = "POI描述最长500字符")
    private String description;

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
}
