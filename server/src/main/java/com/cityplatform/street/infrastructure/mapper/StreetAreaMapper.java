package com.cityplatform.street.infrastructure.mapper;

import com.cityplatform.street.domain.StreetArea;
import org.apache.ibatis.annotations.Mapper;

/**
 * 街区写模型 Mapper。
 */
@Mapper
public interface StreetAreaMapper {

    void insert(StreetArea streetArea);

    void update(StreetArea streetArea);

    StreetArea selectById(Long id);
}
