package com.cityplatform.merchant.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * 优惠券状态变更请求。合法流转 ACTIVE <-> INACTIVE。
 */
public class ChangeCouponStatusRequest {

    @NotBlank(message = "状态不能为空")
    @Pattern(regexp = "ACTIVE|INACTIVE", message = "优惠券状态非法")
    private String status;

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
