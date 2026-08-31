package com.cityplatform.activity.domain;

/**
 * 活动类型。Demo 阶段采用静态枚举，不作为可运营字典维护。
 */
public enum ActivityType {
    FESTIVAL,
    PERFORMANCE,
    EXHIBITION,
    PROMOTION,
    CULTURE,
    OTHER;

    public static ActivityType from(String value) {
        if (value == null) {
            throw new IllegalArgumentException("活动类型不能为空");
        }
        try {
            return ActivityType.valueOf(value);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("非法活动类型: " + value);
        }
    }
}
