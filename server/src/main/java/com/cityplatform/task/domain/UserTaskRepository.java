package com.cityplatform.task.domain;

/**
 * 用户任务写模型 Repository 接口（Domain 层定义，Infrastructure 实现）。
 * markCompleted/markRewardIssued 采用条件更新兜底并发重复完成/重复发奖。
 */
public interface UserTaskRepository {

    void insert(UserTask userTask);

    /**
     * 条件完成更新：仅当当前状态为 JOINED 时置为 COMPLETED。
     * 返回 0 表示已被并发完成，由 Application 转为"任务已完成"业务错误。
     */
    int markCompleted(UserTask userTask);

    /**
     * 条件奖励标记：仅当 reward_issued=0 时置 1。返回 0 表示已发过。
     */
    int markRewardIssued(Long userTaskId);

    UserTask findById(Long id);

    UserTask findByUserIdAndTaskId(Long userId, Long taskId);

    UserTask findByTaskCode(String taskCode);
}
