package com.cityplatform.task.domain;

import com.cityplatform.platform.exception.BusinessException;

import java.time.LocalDateTime;

/**
 * 用户任务 Entity（独立参与关系）。不挂在 User 或 Task 上的从属集合。
 * 一个用户同一任务仅一条记录（数据库 UNIQUE(user_id, task_id) 兜底）。
 * taskCode 参与时生成、全局唯一，到店后由商户输入完成验证（复用核销码模式）。
 */
public class UserTask {

    private Long id;
    private Long taskId;
    private Long userId;
    private UserTaskStatus status;
    private String taskCode;
    private LocalDateTime completedAt;
    private boolean rewardIssued;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * 工厂方法：用户参与任务。
     */
    public static UserTask join(Long taskId, Long userId, String taskCode) {
        UserTask ut = new UserTask();
        ut.taskId = taskId;
        ut.userId = userId;
        ut.status = UserTaskStatus.JOINED;
        ut.taskCode = taskCode;
        ut.rewardIssued = false;
        LocalDateTime now = LocalDateTime.now();
        ut.createdAt = now;
        ut.updatedAt = now;
        return ut;
    }

    /**
     * Domain 规则：是否可完成。仅 JOINED 可完成。
     */
    public boolean canComplete() {
        return status == UserTaskStatus.JOINED;
    }

    /**
     * 完成：JOINED -> COMPLETED，记录完成时间。奖励发放由 Application 层
     * 在同一事务内协调 User 域积分能力，Domain 不跨域。
     */
    public void complete() {
        if (!canComplete()) {
            throw new BusinessException("TASK_ALREADY_COMPLETED", "任务已完成，不可重复完成");
        }
        this.status = UserTaskStatus.COMPLETED;
        this.completedAt = LocalDateTime.now();
        this.updatedAt = this.completedAt;
    }

    /**
     * 标记奖励已发放（Application 在积分到账后调用）。
     */
    public void markRewardIssued() {
        this.rewardIssued = true;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Domain 规则：是否需要发放奖励。已完成且未发过奖励且奖励值大于 0。
     * 奖励配置由 Application 传入（跨实体数据不内嵌本 Entity）。
     */
    public boolean rewardPending(RewardType rewardType, int rewardValue) {
        return status == UserTaskStatus.COMPLETED
                && !rewardIssued
                && rewardType == RewardType.POINT
                && rewardValue > 0;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getTaskId() { return taskId; }
    public void setTaskId(Long taskId) { this.taskId = taskId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public UserTaskStatus getStatus() { return status; }
    public void setStatus(UserTaskStatus status) { this.status = status; }
    public String getTaskCode() { return taskCode; }
    public void setTaskCode(String taskCode) { this.taskCode = taskCode; }
    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
    public boolean isRewardIssued() { return rewardIssued; }
    public void setRewardIssued(boolean rewardIssued) { this.rewardIssued = rewardIssued; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
