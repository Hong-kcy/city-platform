package com.cityplatform.user.domain;

/**
 * 用户状态。Demo 阶段仅保留软删除语义，无管理端状态流转。
 */
public enum UserStatus {
    ACTIVE,
    INACTIVE
}
