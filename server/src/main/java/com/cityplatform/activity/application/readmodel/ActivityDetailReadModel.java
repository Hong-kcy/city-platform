package com.cityplatform.activity.application.readmodel;

import java.time.LocalDateTime;

/**
 * 活动详情查询模型。跨域聚合视图（Application/Query 层组装）：
 * 包含活动本体、街区摘要信息（名称）、POI 摘要信息（名称/类型，未关联时为 null）、
 * 封面 URL 以及当前用户订阅状态（匿名或未订阅时为 null）。
 * Street/User Entity 不进入 Activity Domain，聚合只发生在查询层。
 */
public class ActivityDetailReadModel {

    private Long id;
    private Long streetAreaId;
    private String streetAreaName;
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
    private String poiName;
    private String poiType;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /** 当前用户订阅状态(ACTIVE/CANCELLED)，匿名访问或未订阅为 null */
    private String subscriptionStatus;

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
    public String getPoiName() { return poiName; }
    public void setPoiName(String poiName) { this.poiName = poiName; }
    public String getPoiType() { return poiType; }
    public void setPoiType(String poiType) { this.poiType = poiType; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public String getSubscriptionStatus() { return subscriptionStatus; }
    public void setSubscriptionStatus(String subscriptionStatus) { this.subscriptionStatus = subscriptionStatus; }
}
