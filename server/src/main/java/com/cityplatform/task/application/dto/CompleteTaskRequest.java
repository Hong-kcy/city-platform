package com.cityplatform.task.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 商户完成任务验证请求（输入任务核销码，兼容扫码枪键盘输入）。
 */
public class CompleteTaskRequest {

    @NotBlank(message = "任务核销码不能为空")
    @Size(max = 20, message = "任务核销码最长20字符")
    private String taskCode;

    /** 核销门店，便于追溯；可空。TODO: 商户端鉴权后改为登录态门店 */
    private Long storeId;

    public String getTaskCode() { return taskCode; }
    public void setTaskCode(String taskCode) { this.taskCode = taskCode; }
    public Long getStoreId() { return storeId; }
    public void setStoreId(Long storeId) { this.storeId = storeId; }
}
