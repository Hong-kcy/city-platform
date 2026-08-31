package com.cityplatform.user.domain;

import java.time.LocalDateTime;

/**
 * 用户 Entity。纯 POJO，无框架注解，通过 MyBatis XML 映射。
 * 职责：微信身份绑定、基础资料、轻量画像、消息偏好、积分能力预留。
 * 登录链路（code2Session 调用）属于 Platform Authentication，不由本域感知。
 */
public class User {

    private Long id;
    private String openid;
    private String unionid;
    private String nickname;
    private Long avatarFileId;
    private String preferredActivityTypes;
    private String preferredExperienceTypes;
    private boolean activityReminderEnabled;
    private boolean systemNotificationEnabled;
    private long points;
    private UserStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * 工厂方法：微信登录首次建立用户。
     * 昵称头像允许为空（微信授权资料由用户后续完善），不阻塞身份建立。
     */
    public static User register(String openid, String unionid) {
        User u = new User();
        u.openid = openid;
        u.unionid = unionid;
        u.activityReminderEnabled = true;
        u.systemNotificationEnabled = true;
        u.points = 0;
        u.status = UserStatus.ACTIVE;
        LocalDateTime now = LocalDateTime.now();
        u.createdAt = now;
        u.updatedAt = now;
        return u;
    }

    /**
     * 重复登录时补充 unionid（仅当首次获得且本地为空时记录）。
     */
    public void fillUnionid(String unionid) {
        if (this.unionid == null && unionid != null) {
            this.unionid = unionid;
            this.updatedAt = LocalDateTime.now();
        }
    }

    /**
     * 更新基础资料与轻量画像。null 表示不修改（部分更新语义）。
     */
    public void updateProfile(String nickname, Long avatarFileId,
                              String preferredActivityTypes, String preferredExperienceTypes) {
        if (nickname != null) {
            this.nickname = nickname;
        }
        if (avatarFileId != null) {
            this.avatarFileId = avatarFileId;
        }
        if (preferredActivityTypes != null) {
            this.preferredActivityTypes = preferredActivityTypes;
        }
        if (preferredExperienceTypes != null) {
            this.preferredExperienceTypes = preferredExperienceTypes;
        }
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 更新消息偏好。null 表示不修改。
     */
    public void updateMessagePreference(Boolean activityReminderEnabled,
                                        Boolean systemNotificationEnabled) {
        if (activityReminderEnabled != null) {
            this.activityReminderEnabled = activityReminderEnabled;
        }
        if (systemNotificationEnabled != null) {
            this.systemNotificationEnabled = systemNotificationEnabled;
        }
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 累加积分（最小积分能力，供任务奖励等跨域用例经 Application 层调用）。
     */
    public void addPoints(long delta) {
        if (delta < 0) {
            throw new IllegalArgumentException("积分增量不能为负");
        }
        this.points += delta;
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getOpenid() { return openid; }
    public void setOpenid(String openid) { this.openid = openid; }
    public String getUnionid() { return unionid; }
    public void setUnionid(String unionid) { this.unionid = unionid; }
    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public Long getAvatarFileId() { return avatarFileId; }
    public void setAvatarFileId(Long avatarFileId) { this.avatarFileId = avatarFileId; }
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
    public UserStatus getStatus() { return status; }
    public void setStatus(UserStatus status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
