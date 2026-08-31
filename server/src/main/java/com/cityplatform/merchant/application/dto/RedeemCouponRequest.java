package com.cityplatform.merchant.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 商户核销优惠券请求。Demo 阶段采用核销码手动输入（兼容扫码枪键盘输入），
 * 不实现 Web 摄像头扫码。
 */
public class RedeemCouponRequest {

    @NotBlank(message = "核销码不能为空")
    @Size(max = 20, message = "核销码最长20字符")
    private String redeemCode;

    /** 核销门店，便于追溯；可空。TODO: 商户端鉴权后改为登录态门店 */
    private Long storeId;

    public String getRedeemCode() { return redeemCode; }
    public void setRedeemCode(String redeemCode) { this.redeemCode = redeemCode; }
    public Long getStoreId() { return storeId; }
    public void setStoreId(Long storeId) { this.storeId = storeId; }
}
