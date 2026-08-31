package com.cityplatform.merchant.infrastructure.mapper;

import com.cityplatform.merchant.application.readmodel.UserCouponDetailReadModel;
import com.cityplatform.merchant.application.readmodel.UserCouponSummary;
import com.cityplatform.platform.web.PageParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户优惠券读模型查询 Mapper（JOIN 优惠券/门店/商户，避免 N+1）。
 */
@Mapper
public interface UserCouponQueryMapper {

    UserCouponSummary selectByUserIdAndCouponId(@Param("userId") Long userId,
                                                @Param("couponId") Long couponId);

    List<UserCouponSummary> selectMyCoupons(@Param("userId") Long userId, @Param("page") PageParam page);

    long countMyCoupons(@Param("userId") Long userId);

    UserCouponDetailReadModel selectMyCouponDetail(@Param("userId") Long userId,
                                                   @Param("userCouponId") Long userCouponId);
}
