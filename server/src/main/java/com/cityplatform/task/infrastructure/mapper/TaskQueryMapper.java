package com.cityplatform.task.infrastructure.mapper;

import com.cityplatform.platform.web.PageParam;
import com.cityplatform.task.application.dto.TaskQuery;
import com.cityplatform.task.application.readmodel.TaskDetailReadModel;
import com.cityplatform.task.application.readmodel.TaskSummary;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 任务读模型查询 Mapper（LEFT JOIN 门店组装展示字段，弱关联可空）。
 */
@Mapper
public interface TaskQueryMapper {

    TaskSummary selectById(Long id);

    TaskDetailReadModel selectDetailById(Long id);

    List<TaskSummary> selectAll(@Param("query") TaskQuery query, @Param("page") PageParam page);

    long count(@Param("query") TaskQuery query);
}
