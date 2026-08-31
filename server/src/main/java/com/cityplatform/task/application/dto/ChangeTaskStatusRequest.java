package com.cityplatform.task.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * 任务状态变更请求。合法流转 DRAFT -> ACTIVE、ACTIVE -> DISABLED。
 */
public class ChangeTaskStatusRequest {

    @NotBlank(message = "状态不能为空")
    @Pattern(regexp = "ACTIVE|DISABLED", message = "任务状态非法")
    private String status;

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
