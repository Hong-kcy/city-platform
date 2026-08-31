package com.cityplatform.merchant.infrastructure.mapper;

import com.cityplatform.merchant.application.dto.MerchantQuery;
import com.cityplatform.merchant.application.readmodel.MerchantReadModel;
import com.cityplatform.merchant.application.readmodel.MerchantSummary;
import com.cityplatform.platform.web.PageParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商户查询 Mapper（可用 JOIN）。resultType 映射 ReadModel，不进入 Domain。
 */
@Mapper
public interface MerchantQueryMapper {

    MerchantReadModel selectById(@Param("id") Long id);

    List<MerchantSummary> selectAll(@Param("query") MerchantQuery query, @Param("page") PageParam page);

    long count(@Param("query") MerchantQuery query);
}
