package com.cityplatform.merchant.infrastructure;

import com.cityplatform.merchant.domain.UserCoupon;
import com.cityplatform.merchant.domain.UserCouponRepository;
import com.cityplatform.merchant.infrastructure.mapper.UserCouponMapper;
import org.springframework.stereotype.Repository;

/**
 * 用户优惠券写模型 Repository 实现。
 */
@Repository
public class UserCouponRepositoryImpl implements UserCouponRepository {

    private final UserCouponMapper mapper;

    public UserCouponRepositoryImpl(UserCouponMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void insert(UserCoupon userCoupon) {
        mapper.insert(userCoupon);
    }

    @Override
    public int markRedeemed(UserCoupon userCoupon) {
        return mapper.markRedeemed(userCoupon);
    }

    @Override
    public UserCoupon findById(Long id) {
        return mapper.selectById(id);
    }

    @Override
    public UserCoupon findByUserIdAndCouponId(Long userId, Long couponId) {
        return mapper.selectByUserIdAndCouponId(userId, couponId);
    }

    @Override
    public UserCoupon findByRedeemCode(String redeemCode) {
        return mapper.selectByRedeemCode(redeemCode);
    }
}
