package com.cityplatform.user.application.readmodel;

import java.time.LocalDateTime;

/**
 * 用户查询模型。不暴露 openid/unionid（微信侧身份标识，前端无使用场景）。
 */
public class UserReadModel {

    private Long id;
    private String nickname;
    private String avatarUrl;
    private String preferredActivityTypes;
    private String preferredExperienceTypes;
    private boolean activityReminderEnabled;
    private boolean systemNotificationEnabled;
    private long points;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
    public String getPreferredActivityTypes() { return preferredActivityTypes; }
    public void setPreferredActivityTypes(String preferredActivityTypes) { this.preferredActivityTypes = preferredActivityTypes; }
    public String getPreferredExperienceTypes() { return preferredExperienceTypes; }
    public void setPreferredExperienceTypes(String preferredExperienceTypes) { this.preferredExperienceTypes = preferredExperienceTypes; }
    public boolean isActivityReminderEnabled() { return activityReminderEnabled; }
    public void setActivityReminderEnabled(boolean activityReminderEnabled) { this.activityReminderEnabled = activityReminderEnabled; }
    public boolean isSystemNotificationEnabled() { return systemNotificationEnabled; }
    public void setSystemNotificationEnabled(boolean systemNotificationEnabled) { this.systemNotificationEnabled = systemNotificationEnabled; }
    public long getPoints() { return points; }
    public void setPoints(long points) { this.points = points; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
