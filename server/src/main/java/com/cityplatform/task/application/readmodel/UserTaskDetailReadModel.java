package com.cityplatform.task.application.readmodel;

/**
 * 用户任务详情查询模型。任务核销码仅在本人详情接口暴露（登录态保护）。
 */
public class UserTaskDetailReadModel extends UserTaskSummary {

    /** 任务核销码：到店出示，商户后台输入完成验证 */
    private String taskCode;

    public String getTaskCode() { return taskCode; }
    public void setTaskCode(String taskCode) { this.taskCode = taskCode; }
}
