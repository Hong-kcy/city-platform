package com.cityplatform.merchant.infrastructure.mapper;

import com.cityplatform.merchant.application.dto.StoreQuery;
import com.cityplatform.merchant.application.readmodel.StoreReadModel;
import com.cityplatform.merchant.application.readmodel.StoreSummary;
import com.cityplatform.platform.web.PageParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 门店查询 Mapper。JOIN merchant 取 merchantName；LEFT JOIN stored_file 取 coverImageUrl。
 */
@Mapper
public interface StoreQueryMapper {

    StoreReadModel selectById(@Param("id") Long id);

    List<StoreSummary> selectAll(@Param("query") StoreQuery query, @Param("page") PageParam page);

    long count(@Param("query") StoreQuery query);
}
