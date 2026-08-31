package com.cityplatform.merchant.infrastructure.mapper;

import com.cityplatform.merchant.application.dto.CouponQuery;
import com.cityplatform.merchant.application.readmodel.CouponDetailReadModel;
import com.cityplatform.merchant.application.readmodel.CouponSummary;
import com.cityplatform.platform.web.PageParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 优惠券读模型查询 Mapper（JOIN 门店/商户组装跨域字段）。
 */
@Mapper
public interface CouponQueryMapper {

    CouponSummary selectById(Long id);

    CouponDetailReadModel selectDetailById(Long id);

    List<CouponSummary> selectAll(@Param("query") CouponQuery query, @Param("page") PageParam page);

    long count(@Param("query") CouponQuery query);
}
