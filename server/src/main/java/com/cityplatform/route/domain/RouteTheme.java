package com.cityplatform.route.domain;

/**
 * 路线主题。Demo 阶段采用静态枚举，不作为可运营字典维护。
 */
public enum RouteTheme {
    FRIEND_PHOTO,
    SOLO_RELAX,
    FAMILY_FUN,
    SLOW_WALK,
    OTHER;

    public static RouteTheme from(String value) {
        if (value == null) {
            throw new IllegalArgumentException("路线主题不能为空");
        }
        try {
            return RouteTheme.valueOf(value);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("非法路线主题: " + value);
        }
    }
}
