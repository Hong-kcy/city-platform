package com.cityplatform.task.infrastructure;

import com.cityplatform.platform.web.PageParam;
import com.cityplatform.task.application.TaskQueryRepository;
import com.cityplatform.task.application.dto.TaskQuery;
import com.cityplatform.task.application.readmodel.TaskDetailReadModel;
import com.cityplatform.task.application.readmodel.TaskSummary;
import com.cityplatform.task.infrastructure.mapper.TaskQueryMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 任务读模型 Repository 实现。
 */
@Repository
public class TaskQueryRepositoryImpl implements TaskQueryRepository {

    private final TaskQueryMapper mapper;

    public TaskQueryRepositoryImpl(TaskQueryMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public TaskSummary findById(Long id) {
        return mapper.selectById(id);
    }

    @Override
    public TaskDetailReadModel findDetailById(Long id) {
        return mapper.selectDetailById(id);
    }

    @Override
    public List<TaskSummary> findAll(TaskQuery query, PageParam page) {
        return mapper.selectAll(query, page);
    }

    @Override
    public long count(TaskQuery query) {
        return mapper.count(query);
    }
}
