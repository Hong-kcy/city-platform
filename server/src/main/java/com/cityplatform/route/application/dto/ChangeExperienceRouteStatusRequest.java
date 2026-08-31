package com.cityplatform.route.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * 路线状态变更请求。合法目标：ACTIVE / INACTIVE。
 */
public class ChangeExperienceRouteStatusRequest {

    @NotBlank(message = "状态不能为空")
    @Pattern(regexp = "ACTIVE|INACTIVE", message = "路线状态非法")
    private String status;

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
