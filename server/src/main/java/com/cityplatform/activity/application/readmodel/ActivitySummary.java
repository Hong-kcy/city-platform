package com.cityplatform.activity.application.readmodel;

import java.time.LocalDateTime;

/**
 * 活动列表项查询模型。只读，精简字段，附街区名称与封面 URL（JOIN 组装）。
 */
public class ActivitySummary {

    private Long id;
    private Long streetAreaId;
    private String streetAreaName;
    private String title;
    private String summary;
    private String coverImageUrl;
    private String activityType;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getStreetAreaId() { return streetAreaId; }
    public void setStreetAreaId(Long streetAreaId) { this.streetAreaId = streetAreaId; }
    public String getStreetAreaName() { return streetAreaName; }
    public void setStreetAreaName(String streetAreaName) { this.streetAreaName = streetAreaName; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }
    public String getCoverImageUrl() { return coverImageUrl; }
    public void setCoverImageUrl(String coverImageUrl) { this.coverImageUrl = coverImageUrl; }
    public String getActivityType() { return activityType; }
    public void setActivityType(String activityType) { this.activityType = activityType; }
    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
