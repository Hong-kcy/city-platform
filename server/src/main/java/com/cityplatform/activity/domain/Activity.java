package com.cityplatform.activity.domain;

import com.cityplatform.platform.exception.BusinessException;
import com.cityplatform.platform.exception.IllegalStatusTransitionException;

import java.time.LocalDateTime;

/**
 * 活动 Entity。纯 POJO，无框架注解，通过 MyBatis XML 映射。
 * 活动为独立 Domain，不属于 Merchant 的附属字段；
 * 活动可发生在整个街区（streetAreaId 必填）或具体地点（poiId 可选弱关联）。
 * 状态生命周期 DRAFT -> PUBLISHED -> OFFLINE 由本 Entity 维护。
 */
public class Activity {

    private Long id;
    private Long streetAreaId;
    private String title;
    private String summary;
    private String description;
    private Long coverFileId;
    private ActivityType activityType;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String location;
    private Long poiId;
    private ActivityStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * 工厂方法：创建活动。新活动默认 DRAFT，需发布后对用户可见/可订阅。
     */
    public static Activity create(Long streetAreaId, String title, String summary, String description,
                                   Long coverFileId, ActivityType activityType,
                                   LocalDateTime startTime, LocalDateTime endTime,
                                   String location, Long poiId) {
        validateTimeRange(startTime, endTime);
        Activity a = new Activity();
        a.streetAreaId = streetAreaId;
        a.title = title;
        a.summary = summary;
        a.description = description;
        a.coverFileId = coverFileId;
        a.activityType = activityType;
        a.startTime = startTime;
        a.endTime = endTime;
        a.location = location;
        a.poiId = poiId;
        a.status = ActivityStatus.DRAFT;
        LocalDateTime now = LocalDateTime.now();
        a.createdAt = now;
        a.updatedAt = now;
        return a;
    }

    /**
     * 修改活动信息（PUT 全量替换语义）。已下线活动不允许再修改。
     */
    public void updateInfo(Long streetAreaId, String title, String summary, String description,
                           Long coverFileId, ActivityType activityType,
                           LocalDateTime startTime, LocalDateTime endTime,
                           String location, Long poiId) {
        if (this.status == ActivityStatus.OFFLINE) {
            throw new IllegalStatusTransitionException("已下线活动不允许修改");
        }
        validateTimeRange(startTime, endTime);
        this.streetAreaId = streetAreaId;
        this.title = title;
        this.summary = summary;
        this.description = description;
        this.coverFileId = coverFileId;
        this.activityType = activityType;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
        this.poiId = poiId;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 发布：仅 DRAFT -> PUBLISHED。
     */
    public void publish() {
        if (this.status != ActivityStatus.DRAFT) {
            throw new IllegalStatusTransitionException("仅草稿状态可发布，当前状态: " + this.status);
        }
        this.status = ActivityStatus.PUBLISHED;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 下线：仅 PUBLISHED -> OFFLINE。下线为软操作，不物理删除，已有订阅保留。
     */
    public void takeOffline() {
        if (this.status != ActivityStatus.PUBLISHED) {
            throw new IllegalStatusTransitionException("仅已发布状态可下线，当前状态: " + this.status);
        }
        this.status = ActivityStatus.OFFLINE;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 是否可被用户订阅（"想去"）。Domain 规则：仅已发布活动可订阅。
     */
    public boolean subscribable() {
        return this.status == ActivityStatus.PUBLISHED;
    }

    private static void validateTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        if (startTime == null || endTime == null || !endTime.isAfter(startTime)) {
            throw new BusinessException("INVALID_ACTIVITY_TIME", "活动结束时间必须晚于开始时间");
        }
    }

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
    public ActivityType getActivityType() { return activityType; }
    public void setActivityType(ActivityType activityType) { this.activityType = activityType; }
    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public Long getPoiId() { return poiId; }
    public void setPoiId(Long poiId) { this.poiId = poiId; }
    public ActivityStatus getStatus() { return status; }
    public void setStatus(ActivityStatus status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
