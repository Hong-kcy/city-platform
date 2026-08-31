package com.cityplatform.task.infrastructure;

import com.cityplatform.task.domain.Task;
import com.cityplatform.task.domain.TaskRepository;
import com.cityplatform.task.infrastructure.mapper.TaskMapper;
import org.springframework.stereotype.Repository;

/**
 * 任务写模型 Repository 实现。
 */
@Repository
public class TaskRepositoryImpl implements TaskRepository {

    private final TaskMapper mapper;

    public TaskRepositoryImpl(TaskMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void insert(Task task) {
        mapper.insert(task);
    }

    @Override
    public void update(Task task) {
        mapper.update(task);
    }

    @Override
    public Task findById(Long id) {
        return mapper.selectById(id);
    }
}
