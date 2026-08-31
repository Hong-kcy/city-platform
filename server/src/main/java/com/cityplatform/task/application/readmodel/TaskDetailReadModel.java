package com.cityplatform.task.application.readmodel;

/**
 * 任务详情查询模型。携带有效登录态时附带当前用户参与状态。
 */
public class TaskDetailReadModel extends TaskSummary {

    /** 当前用户的参与状态：JOINED/COMPLETED/null(未参与) */
    private String myStatus;

    public String getMyStatus() { return myStatus; }
    public void setMyStatus(String myStatus) { this.myStatus = myStatus; }
}
