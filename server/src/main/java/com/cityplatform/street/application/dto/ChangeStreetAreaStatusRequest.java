package com.cityplatform.street.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class ChangeStreetAreaStatusRequest {

    @NotBlank(message = "状态不能为空")
    @Pattern(regexp = "ACTIVE|INACTIVE", message = "街区状态非法")
    private String status;

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
