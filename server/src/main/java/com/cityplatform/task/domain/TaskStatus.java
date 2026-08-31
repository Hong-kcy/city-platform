package com.cityplatform.task.domain;

/**
 * 任务状态。存储态仅 DRAFT/ACTIVE/DISABLED，保持最小状态集；
 * PENDING/ENDED 为读时按时间窗口计算，不落库。
 */
public enum TaskStatus {
    DRAFT,
    ACTIVE,
    DISABLED
}
