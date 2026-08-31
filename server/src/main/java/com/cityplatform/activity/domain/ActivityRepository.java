package com.cityplatform.activity.domain;

/**
 * 活动写模型 Repository 接口（Domain 层定义，Infrastructure 实现）。
 */
public interface ActivityRepository {

    void insert(Activity activity);

    void update(Activity activity);

    Activity findById(Long id);
}
