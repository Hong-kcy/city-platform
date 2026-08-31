package com.cityplatform.route.application;

import com.cityplatform.route.application.dto.CreateExperienceRouteRequest;
import com.cityplatform.route.application.dto.UpdateExperienceRouteRequest;
import com.cityplatform.route.domain.ExperienceRoute;
import com.cityplatform.route.domain.RouteTheme;
import org.springframework.stereotype.Component;

/**
 * 路线 DTO/Entity 转换器。DTO 转换统一放 Assembler，Controller/Domain 不负责。
 */
@Component
public class ExperienceRouteAssembler {

    public ExperienceRoute toDomain(CreateExperienceRouteRequest req) {
        return ExperienceRoute.create(
                req.getStreetAreaId(),
                req.getName(),
                RouteTheme.from(req.getTheme()),
                req.getDescription(),
                req.getEstimatedDuration()
        );
    }

    public void applyUpdate(ExperienceRoute route, UpdateExperienceRouteRequest req) {
        route.updateInfo(
                req.getName(),
                RouteTheme.from(req.getTheme()),
                req.getDescription(),
                req.getEstimatedDuration()
        );
    }
}
