package com.cityplatform.task.domain;

/**
 * 用户任务状态。最小状态集：参与/完成。一次性任务不可重复完成。
 */
public enum UserTaskStatus {
    JOINED,
    COMPLETED
}
