package com.cityplatform.recommendation.application.readmodel;

import java.time.LocalDateTime;

/**
 * 活动候选读模型（跨域聚合 SQL 投影，仅供推荐候选准备使用）。
 */
public class ActivityCandidateReadModel {

    private Long id;
    private String title;
    private String activityType;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String coverImageUrl;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getActivityType() { return activityType; }
    public void setActivityType(String activityType) { this.activityType = activityType; }
    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    public String getCoverImageUrl() { return coverImageUrl; }
    public void setCoverImageUrl(String coverImageUrl) { this.coverImageUrl = coverImageUrl; }
}
