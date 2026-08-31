package com.cityplatform.merchant.infrastructure;

import com.cityplatform.merchant.application.UserCouponQueryRepository;
import com.cityplatform.merchant.application.readmodel.UserCouponDetailReadModel;
import com.cityplatform.merchant.application.readmodel.UserCouponSummary;
import com.cityplatform.merchant.infrastructure.mapper.UserCouponQueryMapper;
import com.cityplatform.platform.web.PageParam;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户优惠券读模型 Repository 实现。
 */
@Repository
public class UserCouponQueryRepositoryImpl implements UserCouponQueryRepository {

    private final UserCouponQueryMapper mapper;

    public UserCouponQueryRepositoryImpl(UserCouponQueryMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public UserCouponSummary findByUserIdAndCouponId(Long userId, Long couponId) {
        return mapper.selectByUserIdAndCouponId(userId, couponId);
    }

    @Override
    public List<UserCouponSummary> findMyCoupons(Long userId, PageParam page) {
        return mapper.selectMyCoupons(userId, page);
    }

    @Override
    public long countMyCoupons(Long userId) {
        return mapper.countMyCoupons(userId);
    }

    @Override
    public UserCouponDetailReadModel findMyCouponDetail(Long userId, Long userCouponId) {
        return mapper.selectMyCouponDetail(userId, userCouponId);
    }
}
