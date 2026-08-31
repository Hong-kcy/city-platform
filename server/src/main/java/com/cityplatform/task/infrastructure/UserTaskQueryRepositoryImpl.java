package com.cityplatform.task.infrastructure;

import com.cityplatform.platform.web.PageParam;
import com.cityplatform.task.application.UserTaskQueryRepository;
import com.cityplatform.task.application.readmodel.UserTaskDetailReadModel;
import com.cityplatform.task.application.readmodel.UserTaskSummary;
import com.cityplatform.task.infrastructure.mapper.UserTaskQueryMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户任务读模型 Repository 实现。
 */
@Repository
public class UserTaskQueryRepositoryImpl implements UserTaskQueryRepository {

    private final UserTaskQueryMapper mapper;

    public UserTaskQueryRepositoryImpl(UserTaskQueryMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public String findMyStatus(Long userId, Long taskId) {
        return mapper.selectMyStatus(userId, taskId);
    }

    @Override
    public List<UserTaskSummary> findMyTasks(Long userId, PageParam page) {
        return mapper.selectMyTasks(userId, page);
    }

    @Override
    public long countMyTasks(Long userId) {
        return mapper.countMyTasks(userId);
    }

    @Override
    public UserTaskDetailReadModel findMyTaskDetail(Long userId, Long userTaskId) {
        return mapper.selectMyTaskDetail(userId, userTaskId);
    }
}
