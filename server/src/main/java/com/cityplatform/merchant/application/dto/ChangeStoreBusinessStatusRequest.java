package com.cityplatform.merchant.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class ChangeStoreBusinessStatusRequest {

    @NotBlank(message = "营业状态不能为空")
    @Pattern(regexp = "OPEN|CLOSED", message = "营业状态非法")
    private String businessStatus;

    public String getBusinessStatus() { return businessStatus; }
    public void setBusinessStatus(String businessStatus) { this.businessStatus = businessStatus; }
}
