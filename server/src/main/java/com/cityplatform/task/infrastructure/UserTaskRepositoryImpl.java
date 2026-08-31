package com.cityplatform.task.infrastructure;

import com.cityplatform.task.domain.UserTask;
import com.cityplatform.task.domain.UserTaskRepository;
import com.cityplatform.task.infrastructure.mapper.UserTaskMapper;
import org.springframework.stereotype.Repository;

/**
 * 用户任务写模型 Repository 实现。
 */
@Repository
public class UserTaskRepositoryImpl implements UserTaskRepository {

    private final UserTaskMapper mapper;

    public UserTaskRepositoryImpl(UserTaskMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void insert(UserTask userTask) {
        mapper.insert(userTask);
    }

    @Override
    public int markCompleted(UserTask userTask) {
        return mapper.markCompleted(userTask);
    }

    @Override
    public int markRewardIssued(Long userTaskId) {
        return mapper.markRewardIssued(userTaskId);
    }

    @Override
    public UserTask findById(Long id) {
        return mapper.selectById(id);
    }

    @Override
    public UserTask findByUserIdAndTaskId(Long userId, Long taskId) {
        return mapper.selectByUserIdAndTaskId(userId, taskId);
    }

    @Override
    public UserTask findByTaskCode(String taskCode) {
        return mapper.selectByTaskCode(taskCode);
    }
}
