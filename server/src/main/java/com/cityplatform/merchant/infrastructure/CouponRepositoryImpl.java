package com.cityplatform.merchant.infrastructure;

import com.cityplatform.merchant.domain.Coupon;
import com.cityplatform.merchant.domain.CouponRepository;
import com.cityplatform.merchant.infrastructure.mapper.CouponMapper;
import org.springframework.stereotype.Repository;

/**
 * 优惠券写模型 Repository 实现。
 */
@Repository
public class CouponRepositoryImpl implements CouponRepository {

    private final CouponMapper mapper;

    public CouponRepositoryImpl(CouponMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void insert(Coupon coupon) {
        mapper.insert(coupon);
    }

    @Override
    public void update(Coupon coupon) {
        mapper.update(coupon);
    }

    @Override
    public Coupon findById(Long id) {
        return mapper.selectById(id);
    }
}
