package com.cityplatform.task.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

/**
 * 创建任务请求。sourceType/sourceId 为来源引用元数据（弱关联，可空 sourceId）。
 */
public class CreateTaskRequest {

    @NotBlank(message = "任务标题不能为空")
    @Size(max = 100, message = "任务标题最长100字符")
    private String title;

    @Size(max = 500, message = "任务说明最长500字符")
    private String description;

    @NotBlank(message = "任务类型不能为空")
    @Pattern(regexp = "STORE_VISIT", message = "任务类型非法")
    private String taskType;

    @NotBlank(message = "来源类型不能为空")
    @Pattern(regexp = "ACTIVITY|RECOMMENDATION|OPERATION|MERCHANT", message = "来源类型非法")
    private String sourceType;

    private Long sourceId;

    private Long storeId;

    @NotBlank(message = "奖励类型不能为空")
    @Pattern(regexp = "POINT", message = "奖励类型非法")
    private String rewardType;

    @NotNull(message = "奖励数值不能为空")
    @PositiveOrZero(message = "奖励数值不能为负")
    private Integer rewardValue;

    @NotNull(message = "开始时间不能为空")
    private LocalDateTime startAt;

    @NotNull(message = "结束时间不能为空")
    private LocalDateTime endAt;

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
    public String getRewardType() { return rewardType; }
    public void setRewardType(String rewardType) { this.rewardType = rewardType; }
    public Integer getRewardValue() { return rewardValue; }
    public void setRewardValue(Integer rewardValue) { this.rewardValue = rewardValue; }
    public LocalDateTime getStartAt() { return startAt; }
    public void setStartAt(LocalDateTime startAt) { this.startAt = startAt; }
    public LocalDateTime getEndAt() { return endAt; }
    public void setEndAt(LocalDateTime endAt) { this.endAt = endAt; }
}
