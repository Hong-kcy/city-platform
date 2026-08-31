package com.cityplatform.user.application.dto;

import jakarta.validation.constraints.Size;

/**
 * 当前用户资料/偏好更新请求。所有字段可选，null 表示不修改（部分更新语义）。
 * 只能修改当前登录用户自己的资料，userId 来自登录态而非请求参数。
 */
public class UpdateMyProfileRequest {

    @Size(max = 100, message = "昵称最长100字符")
    private String nickname;

    private Long avatarFileId;

    @Size(max = 200, message = "偏好活动类型最长200字符")
    private String preferredActivityTypes;

    @Size(max = 200, message = "偏好空间体验类型最长200字符")
    private String preferredExperienceTypes;

    private Boolean activityReminderEnabled;

    private Boolean systemNotificationEnabled;

    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public Long getAvatarFileId() { return avatarFileId; }
    public void setAvatarFileId(Long avatarFileId) { this.avatarFileId = avatarFileId; }
    public String getPreferredActivityTypes() { return preferredActivityTypes; }
    public void setPreferredActivityTypes(String preferredActivityTypes) { this.preferredActivityTypes = preferredActivityTypes; }
    public String getPreferredExperienceTypes() { return preferredExperienceTypes; }
    public void setPreferredExperienceTypes(String preferredExperienceTypes) { this.preferredExperienceTypes = preferredExperienceTypes; }
    public Boolean getActivityReminderEnabled() { return activityReminderEnabled; }
    public void setActivityReminderEnabled(Boolean activityReminderEnabled) { this.activityReminderEnabled = activityReminderEnabled; }
    public Boolean getSystemNotificationEnabled() { return systemNotificationEnabled; }
    public void setSystemNotificationEnabled(Boolean systemNotificationEnabled) { this.systemNotificationEnabled = systemNotificationEnabled; }
}
