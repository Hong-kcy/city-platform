package com.cityplatform.user.infrastructure;

import com.cityplatform.user.application.UserQueryRepository;
import com.cityplatform.user.application.readmodel.UserReadModel;
import com.cityplatform.user.infrastructure.mapper.UserQueryMapper;
import org.springframework.stereotype.Repository;

/**
 * 用户查询模型 Repository 实现。面向 ReadModel，不返回 Domain Entity。
 */
@Repository
public class UserQueryRepositoryImpl implements UserQueryRepository {

    private final UserQueryMapper mapper;

    public UserQueryRepositoryImpl(UserQueryMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public UserReadModel findById(Long id) {
        return mapper.selectById(id);
    }
}
