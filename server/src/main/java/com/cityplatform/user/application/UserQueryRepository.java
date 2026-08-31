package com.cityplatform.user.application;

import com.cityplatform.user.application.readmodel.UserReadModel;

/**
 * 用户查询模型接口（Application 层定义，Infrastructure 实现）。
 * 面向 ReadModel，头像 URL 通过 JOIN stored_file 组装。
 */
public interface UserQueryRepository {

    UserReadModel findById(Long id);
}
