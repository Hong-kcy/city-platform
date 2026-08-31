package com.cityplatform.user.infrastructure.mapper;

import com.cityplatform.user.application.readmodel.UserReadModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户查询 Mapper。面向 ReadModel，头像 URL 通过 JOIN stored_file 组装。
 */
@Mapper
public interface UserQueryMapper {

    UserReadModel selectById(@Param("id") Long id);
}
