package com.cityplatform.user.domain;

/**
 * 用户写模型 Repository 接口（Domain 层定义，Infrastructure 实现）。
 * 仅承担写操作与按主键/openid 读取 Entity，不承担查询投影。
 */
public interface UserRepository {

    void insert(User user);

    void update(User user);

    User findById(Long id);

    User findByOpenid(String openid);
}
