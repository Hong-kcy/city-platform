package com.cityplatform.merchant.infrastructure.mapper;

import com.cityplatform.merchant.domain.Store;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 门店写操作 Mapper（单表）。
 */
@Mapper
public interface StoreMapper {

    int insert(Store store);

    int update(Store store);

    Store selectById(@Param("id") Long id);
}
