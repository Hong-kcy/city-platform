package com.cityplatform.route.domain;

import java.util.List;

/**
 * 路线 POI 关联写模型 Repository 接口（Domain 层定义，Infrastructure 实现）。
 */
public interface ExperienceRouteItemRepository {

    void insert(ExperienceRouteItem item);

    void update(ExperienceRouteItem item);

    void deleteById(Long id);

    /**
     * 查询路线下全部 POI 关联，按 sequence 升序。
     */
    List<ExperienceRouteItem> findByRouteIdOrderBySequence(Long routeId);

    ExperienceRouteItem findByRouteIdAndPoiId(Long routeId, Long poiId);

    /**
     * 将路线下全部关联的 sequence 整体加上偏移量。
     * 批量顺序调整/压缩前的中间态避让手段，须在同一事务内紧跟写入终值。
     */
    void shiftSequence(Long routeId, int offset);
}
