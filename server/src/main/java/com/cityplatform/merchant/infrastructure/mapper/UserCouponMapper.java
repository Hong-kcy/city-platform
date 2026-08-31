package com.cityplatform.merchant.infrastructure.mapper;

import com.cityplatform.merchant.domain.UserCoupon;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户优惠券写操作 Mapper（单表）。
 * markRedeemed 为条件更新，防并发重复核销。
 */
@Mapper
public interface UserCouponMapper {

    int insert(UserCoupon userCoupon);

    int update(UserCoupon userCoupon);

    int markRedeemed(UserCoupon userCoupon);

    UserCoupon selectById(Long id);

    UserCoupon selectByUserIdAndCouponId(@Param("userId") Long userId,
                                         @Param("couponId") Long couponId);

    UserCoupon selectByRedeemCode(String redeemCode);
}
