package com.cityplatform.merchant.domain;

/**
 * 商户写模型 Repository 接口（Domain 层定义，Infrastructure 实现）。
 * 仅承担写操作与按主键读取 Entity，不承担查询投影。
 */
public interface MerchantRepository {

    void insert(Merchant merchant);

    void update(Merchant merchant);

    Merchant findById(Long id);
}
