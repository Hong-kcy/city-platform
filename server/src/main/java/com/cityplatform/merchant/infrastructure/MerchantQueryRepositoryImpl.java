package com.cityplatform.merchant.infrastructure;

import com.cityplatform.merchant.application.MerchantQueryRepository;
import com.cityplatform.merchant.application.dto.MerchantQuery;
import com.cityplatform.merchant.application.readmodel.MerchantReadModel;
import com.cityplatform.merchant.application.readmodel.MerchantSummary;
import com.cityplatform.merchant.infrastructure.mapper.MerchantQueryMapper;
import com.cityplatform.platform.web.PageParam;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 商户查询模型 Repository 实现。实现 Application 层接口，SQL LEFT JOIN 组装 ReadModel。
 */
@Repository
public class MerchantQueryRepositoryImpl implements MerchantQueryRepository {

    private final MerchantQueryMapper mapper;

    public MerchantQueryRepositoryImpl(MerchantQueryMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public MerchantReadModel findById(Long id) {
        return mapper.selectById(id);
    }

    @Override
    public List<MerchantSummary> findAll(MerchantQuery query, PageParam page) {
        return mapper.selectAll(query, page);
    }

    @Override
    public long count(MerchantQuery query) {
        return mapper.count(query);
    }
}
