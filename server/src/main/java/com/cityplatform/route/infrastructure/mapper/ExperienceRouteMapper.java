package com.cityplatform.route.infrastructure.mapper;

import com.cityplatform.route.domain.ExperienceRoute;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 路线写操作 Mapper（单表）。resultType 映射 Domain Entity（纯 POJO）。
 */
@Mapper
public interface ExperienceRouteMapper {

    int insert(ExperienceRoute route);

    int update(ExperienceRoute route);

    ExperienceRoute selectById(@Param("id") Long id);
}
