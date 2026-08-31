package com.cityplatform.merchant.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

/**
 * 创建优惠券请求。商户/门店身份由请求外上下文决定，storeId 必填，
 * merchantId 由门店派生，不信任前端传入。
 */
public class CreateCouponRequest {

    @NotNull(message = "所属门店不能为空")
    private Long storeId;

    @NotBlank(message = "优惠券名称不能为空")
    @Size(max = 100, message = "优惠券名称最长100字符")
    private String name;

    @Size(max = 500, message = "使用说明最长500字符")
    private String description;

    @NotBlank(message = "权益文本不能为空")
    @Size(max = 100, message = "权益文本最长100字符")
    private String discountText;

    @NotNull(message = "有效期开始不能为空")
    private LocalDateTime validFrom;

    @NotNull(message = "有效期结束不能为空")
    private LocalDateTime validTo;

    public Long getStoreId() { return storeId; }
    public void setStoreId(Long storeId) { this.storeId = storeId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getDiscountText() { return discountText; }
    public void setDiscountText(String discountText) { this.discountText = discountText; }
    public LocalDateTime getValidFrom() { return validFrom; }
    public void setValidFrom(LocalDateTime validFrom) { this.validFrom = validFrom; }
    public LocalDateTime getValidTo() { return validTo; }
    public void setValidTo(LocalDateTime validTo) { this.validTo = validTo; }
}
