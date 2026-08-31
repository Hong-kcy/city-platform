package com.cityplatform.merchant.infrastructure;

import com.cityplatform.merchant.domain.Merchant;
import com.cityplatform.merchant.domain.MerchantRepository;
import com.cityplatform.merchant.infrastructure.mapper.MerchantMapper;
import org.springframework.stereotype.Repository;

/**
 * 商户写模型 Repository 实现。实现 Domain 层接口，调用 Mapper。
 */
@Repository
public class MerchantRepositoryImpl implements MerchantRepository {

    private final MerchantMapper mapper;

    public MerchantRepositoryImpl(MerchantMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void insert(Merchant merchant) {
        mapper.insert(merchant);
    }

    @Override
    public void update(Merchant merchant) {
        mapper.update(merchant);
    }

    @Override
    public Merchant findById(Long id) {
        return mapper.selectById(id);
    }
}
