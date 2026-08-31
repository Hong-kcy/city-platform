package com.cityplatform.user.infrastructure.mapper;

import com.cityplatform.user.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户写操作 Mapper（单表）。resultType 映射 Domain Entity（纯 POJO）。
 */
@Mapper
public interface UserMapper {

    int insert(User user);

    int update(User user);

    User selectById(@Param("id") Long id);

    User selectByOpenid(@Param("openid") String openid);
}
