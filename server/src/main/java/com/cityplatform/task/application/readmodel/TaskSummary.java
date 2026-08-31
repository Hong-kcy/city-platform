package com.cityplatform.task.application.readmodel;

import java.time.LocalDateTime;

/**
 * 任务摘要查询模型（列表用）。storeName 由 SQL JOIN 组装（弱关联可空）。
 * effectiveStatus 为读时计算：DRAFT/PENDING/ACTIVE/ENDED/DISABLED。
 */
public class TaskSummary {

    private Long id;
    private String title;
    private String description;
    private String taskType;
    private String sourceType;
    private Long sourceId;
    private Long storeId;
    private String storeName;
    private String rewardType;
    private Integer rewardValue;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private String status;
    private String effectiveStatus;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getTaskType() { return taskType; }
    public void setTaskType(String taskType) { this.taskType = taskType; }
    public String getSourceType() { return sourceType; }
    public void setSourceType(String sourceType) { this.sourceType = sourceType; }
    public Long getSourceId() { return sourceId; }
    public void setSourceId(Long sourceId) { this.sourceId = sourceId; }
    public Long getStoreId() { return storeId; }
    public void setStoreId(Long storeId) { this.storeId = storeId; }
    public String getStoreName() { return storeName; }
    public void setStoreName(String storeName) { this.storeName = storeName; }
    public String getRewardType() { return rewardType; }
    public void setRewardType(String rewardType) { this.rewardType = rewardType; }
    public Integer getRewardValue() { return rewardValue; }
    public void setRewardValue(Integer rewardValue) { this.rewardValue = rewardValue; }
    public LocalDateTime getStartAt() { return startAt; }
    public void setStartAt(LocalDateTime startAt) { this.startAt = startAt; }
    public LocalDateTime getEndAt() { return endAt; }
    public void setEndAt(LocalDateTime endAt) { this.endAt = endAt; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getEffectiveStatus() { return effectiveStatus; }
    public void setEffectiveStatus(String effectiveStatus) { this.effectiveStatus = effectiveStatus; }
}
