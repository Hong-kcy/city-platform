package com.cityplatform.activity.domain;

/**
 * 活动状态生命周期：DRAFT -> PUBLISHED -> OFFLINE。
 * 与 Merchant/Store 的启停语义不同，活动状态表达内容生命周期，
 * 状态流转规则由 Activity Entity 维护，禁止非法跃迁。
 */
public enum ActivityStatus {
    DRAFT,
    PUBLISHED,
    OFFLINE
}
