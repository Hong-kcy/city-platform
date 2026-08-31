package com.cityplatform.merchant.domain;

/**
 * 门店写模型 Repository 接口（Domain 层定义，Infrastructure 实现）。
 */
public interface StoreRepository {

    void insert(Store store);

    void update(Store store);

    Store findById(Long id);
}
