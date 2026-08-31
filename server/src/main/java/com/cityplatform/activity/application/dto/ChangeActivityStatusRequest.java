package com.cityplatform.activity.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * 活动状态变更请求。合法流转目标：PUBLISHED(发布) / OFFLINE(下线)，
 * DRAFT 仅作为初始状态，不提供回退目标。
 */
public class ChangeActivityStatusRequest {

    @NotBlank(message = "状态不能为空")
    @Pattern(regexp = "PUBLISHED|OFFLINE", message = "非法状态，仅支持 PUBLISHED/OFFLINE")
    private String status;

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
