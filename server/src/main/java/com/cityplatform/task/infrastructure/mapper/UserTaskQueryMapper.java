package com.cityplatform.task.infrastructure.mapper;

import com.cityplatform.platform.web.PageParam;
import com.cityplatform.task.application.readmodel.UserTaskDetailReadModel;
import com.cityplatform.task.application.readmodel.UserTaskSummary;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户任务读模型查询 Mapper（JOIN 任务/门店，避免 N+1）。
 */
@Mapper
public interface UserTaskQueryMapper {

    String selectMyStatus(@Param("userId") Long userId, @Param("taskId") Long taskId);

    List<UserTaskSummary> selectMyTasks(@Param("userId") Long userId, @Param("page") PageParam page);

    long countMyTasks(@Param("userId") Long userId);

    UserTaskDetailReadModel selectMyTaskDetail(@Param("userId") Long userId,
                                               @Param("userTaskId") Long userTaskId);
}
