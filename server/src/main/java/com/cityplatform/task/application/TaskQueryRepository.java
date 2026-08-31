package com.cityplatform.task.application;

import com.cityplatform.platform.web.PageParam;
import com.cityplatform.task.application.dto.TaskQuery;
import com.cityplatform.task.application.readmodel.TaskDetailReadModel;
import com.cityplatform.task.application.readmodel.TaskSummary;

import java.util.List;

/**
 * 任务读模型查询接口（Application 层定义，Infrastructure 用 SQL JOIN 实现）。
 */
public interface TaskQueryRepository {

    TaskSummary findById(Long id);

    TaskDetailReadModel findDetailById(Long id);

    List<TaskSummary> findAll(TaskQuery query, PageParam page);

    long count(TaskQuery query);
}
