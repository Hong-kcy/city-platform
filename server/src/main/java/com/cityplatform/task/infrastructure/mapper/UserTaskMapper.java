package com.cityplatform.task.infrastructure.mapper;

import com.cityplatform.task.domain.UserTask;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户任务写操作 Mapper（单表）。
 * markCompleted/markRewardIssued 为条件更新，防并发重复完成/重复发奖。
 */
@Mapper
public interface UserTaskMapper {

    int insert(UserTask userTask);

    int markCompleted(UserTask userTask);

    int markRewardIssued(@Param("userTaskId") Long userTaskId);

    UserTask selectById(Long id);

    UserTask selectByUserIdAndTaskId(@Param("userId") Long userId, @Param("taskId") Long taskId);

    UserTask selectByTaskCode(String taskCode);
}
