package com.cityplatform.merchant.application.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

/**
 * 修改门店请求（PUT 全量替换语义）。
 */
public class UpdateStoreRequest {

    @NotBlank(message = "门店名称不能为空")
    @Size(max = 100, message = "门店名称最长100字符")
    private String name;

    @NotBlank(message = "门店地址不能为空")
    @Size(max = 255, message = "门店地址最长255字符")
    private String address;

    @DecimalMin(value = "-180.0000000", message = "经度非法")
    @DecimalMax(value = "180.0000000", message = "经度非法")
    private BigDecimal longitude;

    @DecimalMin(value = "-90.0000000", message = "纬度非法")
    @DecimalMax(value = "90.0000000", message = "纬度非法")
    private BigDecimal latitude;

    @Size(max = 20, message = "门店电话最长20字符")
    private String phone;

    @Size(max = 100, message = "营业时间最长100字符")
    private String businessHours;

    private Long coverImageFileId;

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
}
