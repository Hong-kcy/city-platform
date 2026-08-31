package com.cityplatform.merchant.infrastructure.mapper;

import com.cityplatform.merchant.domain.Coupon;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券写操作 Mapper（单表）。
 */
@Mapper
public interface CouponMapper {

    int insert(Coupon coupon);

    int update(Coupon coupon);

    Coupon selectById(Long id);
}
