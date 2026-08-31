package com.cityplatform.user.infrastructure;

import com.cityplatform.user.domain.User;
import com.cityplatform.user.domain.UserRepository;
import com.cityplatform.user.infrastructure.mapper.UserMapper;
import org.springframework.stereotype.Repository;

/**
 * 用户写模型 Repository 实现。实现 Domain 层接口，调用 Mapper。
 */
@Repository
public class UserRepositoryImpl implements UserRepository {

    private final UserMapper mapper;

    public UserRepositoryImpl(UserMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void insert(User user) {
        mapper.insert(user);
    }

    @Override
    public void update(User user) {
        mapper.update(user);
    }

    @Override
    public User findById(Long id) {
        return mapper.selectById(id);
    }

    @Override
    public User findByOpenid(String openid) {
        return mapper.selectByOpenid(openid);
    }
}
