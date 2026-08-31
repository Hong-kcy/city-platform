package com.cityplatform.route.domain;

/**
 * 路线写模型 Repository 接口（Domain 层定义，Infrastructure 实现）。
 */
public interface ExperienceRouteRepository {

    void insert(ExperienceRoute route);

    void update(ExperienceRoute route);

    ExperienceRoute findById(Long id);
}
