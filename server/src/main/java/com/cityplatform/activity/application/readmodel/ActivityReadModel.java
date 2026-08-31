package com.cityplatform.activity.application.readmodel;

import java.time.LocalDateTime;

/**
 * 活动查询模型。封面 URL 通过 JOIN stored_file 组装，无封面时回退默认封面。
 */
public class ActivityReadModel {

    private Long id;
    private Long streetAreaId;
    private String title;
    private String summary;
    private String description;
    private Long coverFileId;
    private String coverImageUrl;
    private String activityType;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String location;
    private Long poiId;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
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
    public String getCoverImageUrl() { return coverImageUrl; }
    public void setCoverImageUrl(String coverImageUrl) { this.coverImageUrl = coverImageUrl; }
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
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
