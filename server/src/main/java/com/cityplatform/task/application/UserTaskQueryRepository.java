package com.cityplatform.task.application;

import com.cityplatform.platform.web.PageParam;
import com.cityplatform.task.application.readmodel.UserTaskDetailReadModel;
import com.cityplatform.task.application.readmodel.UserTaskSummary;

import java.util.List;

/**
 * 用户任务读模型查询接口（Application 层定义，Infrastructure 用 SQL JOIN 实现）。
 */
public interface UserTaskQueryRepository {

    String findMyStatus(Long userId, Long taskId);

    List<UserTaskSummary> findMyTasks(Long userId, PageParam page);

    long countMyTasks(Long userId);

    UserTaskDetailReadModel findMyTaskDetail(Long userId, Long userTaskId);
}
