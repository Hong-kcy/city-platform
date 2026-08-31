package com.cityplatform.merchant.infrastructure;

import com.cityplatform.merchant.application.CouponQueryRepository;
import com.cityplatform.merchant.application.dto.CouponQuery;
import com.cityplatform.merchant.application.readmodel.CouponDetailReadModel;
import com.cityplatform.merchant.application.readmodel.CouponSummary;
import com.cityplatform.merchant.infrastructure.mapper.CouponQueryMapper;
import com.cityplatform.platform.web.PageParam;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 优惠券读模型 Repository 实现。
 */
@Repository
public class CouponQueryRepositoryImpl implements CouponQueryRepository {

    private final CouponQueryMapper mapper;

    public CouponQueryRepositoryImpl(CouponQueryMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public CouponSummary findById(Long id) {
        return mapper.selectById(id);
    }

    @Override
    public CouponDetailReadModel findDetailById(Long id) {
        return mapper.selectDetailById(id);
    }

    @Override
    public List<CouponSummary> findAll(CouponQuery query, PageParam page) {
        return mapper.selectAll(query, page);
    }

    @Override
    public long count(CouponQuery query) {
        return mapper.count(query);
    }
}
