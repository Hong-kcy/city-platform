package com.cityplatform.activity.application.dto;

import java.time.LocalDateTime;

/**
 * 活动列表查询条件。支持街区、类型、状态、开始时间范围过滤。
 */
public class ActivityQuery {

    private Long streetAreaId;
    private String activityType;
    private String status;
    private LocalDateTime startTimeFrom;
    private LocalDateTime startTimeTo;

    public Long getStreetAreaId() { return streetAreaId; }
    public void setStreetAreaId(Long streetAreaId) { this.streetAreaId = streetAreaId; }
    public String getActivityType() { return activityType; }
    public void setActivityType(String activityType) { this.activityType = activityType; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getStartTimeFrom() { return startTimeFrom; }
    public void setStartTimeFrom(LocalDateTime startTimeFrom) { this.startTimeFrom = startTimeFrom; }
    public LocalDateTime getStartTimeTo() { return startTimeTo; }
    public void setStartTimeTo(LocalDateTime startTimeTo) { this.startTimeTo = startTimeTo; }
}
