package com.cityplatform.street.domain;

/**
 * 街区写模型 Repository 接口（Domain 层定义，Infrastructure 实现）。
 * 仅承担写操作与按主键读取 Entity，不承担查询投影。
 */
public interface StreetAreaRepository {

    void insert(StreetArea streetArea);

    void update(StreetArea streetArea);

    StreetArea findById(Long id);
}
