package com.cityplatform.task.application.readmodel;

import java.time.LocalDateTime;

/**
 * "我的任务"列表项查询模型。参与信息 + 任务摘要（JOIN 组装，避免 N+1）。
 * taskCode 仅在详情 ReadModel 暴露，列表不返回。
 */
public class UserTaskSummary {

    private Long id;
    private Long taskId;
    private String title;
    private String description;
    private String storeName;
    private String rewardType;
    private Integer rewardValue;
    private String taskStatus;
    private LocalDateTime joinedAt;
    private LocalDateTime completedAt;
    private Boolean rewardIssued;
    private String taskEffectiveStatus;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getTaskId() { return taskId; }
    public void setTaskId(Long taskId) { this.taskId = taskId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getStoreName() { return storeName; }
    public void setStoreName(String storeName) { this.storeName = storeName; }
    public String getRewardType() { return rewardType; }
    public void setRewardType(String rewardType) { this.rewardType = rewardType; }
    public Integer getRewardValue() { return rewardValue; }
    public void setRewardValue(Integer rewardValue) { this.rewardValue = rewardValue; }
    public String getTaskStatus() { return taskStatus; }
    public void setTaskStatus(String taskStatus) { this.taskStatus = taskStatus; }
    public LocalDateTime getJoinedAt() { return joinedAt; }
    public void setJoinedAt(LocalDateTime joinedAt) { this.joinedAt = joinedAt; }
    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
    public Boolean getRewardIssued() { return rewardIssued; }
    public void setRewardIssued(Boolean rewardIssued) { this.rewardIssued = rewardIssued; }
    public String getTaskEffectiveStatus() { return taskEffectiveStatus; }
    public void setTaskEffectiveStatus(String taskEffectiveStatus) { this.taskEffectiveStatus = taskEffectiveStatus; }
}
