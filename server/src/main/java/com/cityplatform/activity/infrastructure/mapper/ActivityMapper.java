package com.cityplatform.activity.infrastructure.mapper;

import com.cityplatform.activity.domain.Activity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 活动写操作 Mapper（单表）。resultType 映射 Domain Entity（纯 POJO）。
 */
@Mapper
public interface ActivityMapper {

    int insert(Activity activity);

    int update(Activity activity);

    Activity selectById(@Param("id") Long id);
}
