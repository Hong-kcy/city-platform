package com.cityplatform.task.domain;

/**
 * 任务写模型 Repository 接口（Domain 层定义，Infrastructure 实现）。
 */
public interface TaskRepository {

    void insert(Task task);

    void update(Task task);

    Task findById(Long id);
}
