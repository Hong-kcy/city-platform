package com.cityplatform.activity.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

/**
 * 修改活动请求（PUT 全量替换语义，与 Merchant 更新一致）。
 */
public class UpdateActivityRequest {

    @NotNull(message = "所属街区不能为空")
    private Long streetAreaId;

    @NotBlank(message = "活动标题不能为空")
    @Size(max = 100, message = "活动标题最长100字符")
    private String title;

    @Size(max = 500, message = "活动摘要最长500字符")
    private String summary;

    private String description;

    private Long coverFileId;

    @NotBlank(message = "活动类型不能为空")
    @Pattern(regexp = "FESTIVAL|PERFORMANCE|EXHIBITION|PROMOTION|CULTURE|OTHER", message = "活动类型非法")
    private String activityType;

    @NotNull(message = "开始时间不能为空")
    private LocalDateTime startTime;

    @NotNull(message = "结束时间不能为空")
    private LocalDateTime endTime;

    @Size(max = 255, message = "活动地点最长255字符")
    private String location;

    private Long poiId;

    public Long getStreetAreaId() { return streetAreaId; }
    public void setStreetAreaId(Long streetAreaId) { this.streetAreaId = streetAreaId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Long getCoverFileId() { return coverFileId; }
    public void setCoverFileId(Long coverFileId) { this.coverFileId = coverFileId; }
    public String getActivityType() { return activityType; }
    public void setActivityType(String activityType) { this.activityType = activityType; }
    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public Long getPoiId() { return poiId; }
    public void setPoiId(Long poiId) { this.poiId = poiId; }
}
