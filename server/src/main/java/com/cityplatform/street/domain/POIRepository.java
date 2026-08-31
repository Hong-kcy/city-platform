package com.cityplatform.street.domain;

/**
 * POI 写模型 Repository 接口（Domain 层定义，Infrastructure 实现）。
 */
public interface POIRepository {

    void insert(POI poi);

    void update(POI poi);

    POI findById(Long id);
}
