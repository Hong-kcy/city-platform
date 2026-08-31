package com.cityplatform.merchant.infrastructure.mapper;

import com.cityplatform.merchant.domain.Merchant;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 商户写操作 Mapper（单表）。resultType 映射 Domain Entity（纯 POJO）。
 */
@Mapper
public interface MerchantMapper {

    int insert(Merchant merchant);

    int update(Merchant merchant);

    Merchant selectById(@Param("id") Long id);
}
