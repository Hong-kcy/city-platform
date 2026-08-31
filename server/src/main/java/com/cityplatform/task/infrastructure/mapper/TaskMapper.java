package com.cityplatform.task.infrastructure.mapper;

import com.cityplatform.task.domain.Task;
import org.apache.ibatis.annotations.Mapper;

/**
 * 任务写操作 Mapper（单表）。
 */
@Mapper
public interface TaskMapper {

    int insert(Task task);

    int update(Task task);

    Task selectById(Long id);
}
