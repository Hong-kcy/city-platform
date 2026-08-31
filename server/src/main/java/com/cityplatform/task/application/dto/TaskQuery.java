package com.cityplatform.task.application.dto;

/**
 * 任务列表查询条件。
 */
public class TaskQuery {

    private Long storeId;
    private String sourceType;
    /** 存储状态过滤：DRAFT/ACTIVE/DISABLED；用户端固定传 ACTIVE */
    private String status;

    public Long getStoreId() { return storeId; }
    public void setStoreId(Long storeId) { this.storeId = storeId; }
    public String getSourceType() { return sourceType; }
    public void setSourceType(String sourceType) { this.sourceType = sourceType; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
