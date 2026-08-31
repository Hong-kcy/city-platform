package com.cityplatform.street.infrastructure.mapper;

import com.cityplatform.street.domain.POI;
import org.apache.ibatis.annotations.Mapper;

/**
 * POI 写模型 Mapper。
 */
@Mapper
public interface POIMapper {

    void insert(POI poi);

    void update(POI poi);

    POI selectById(Long id);
}
