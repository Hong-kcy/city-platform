package com.cityplatform.task.domain;

/**
 * 任务来源类型。来源只是元数据引用，不代表 Task 属于这些 Domain；
 * Task 由任何来源创建后独立运行自己的生命周期。
 */
public enum TaskSource {
    ACTIVITY,
    RECOMMENDATION,
    OPERATION,
    MERCHANT
}
