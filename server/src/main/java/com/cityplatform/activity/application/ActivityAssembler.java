package com.cityplatform.activity.application;

import com.cityplatform.activity.application.dto.CreateActivityRequest;
import com.cityplatform.activity.application.dto.UpdateActivityRequest;
import com.cityplatform.activity.domain.Activity;
import com.cityplatform.activity.domain.ActivityType;
import org.springframework.stereotype.Component;

/**
 * 活动 DTO/Entity 转换器。DTO 转换统一放 Assembler，Controller/Domain 不负责。
 */
@Component
public class ActivityAssembler {

    public Activity toDomain(CreateActivityRequest req) {
        return Activity.create(
                req.getStreetAreaId(),
                req.getTitle(),
                req.getSummary(),
                req.getDescription(),
                req.getCoverFileId(),
                ActivityType.from(req.getActivityType()),
                req.getStartTime(),
                req.getEndTime(),
                req.getLocation(),
                req.getPoiId()
        );
    }

    public void applyUpdate(Activity activity, UpdateActivityRequest req) {
        activity.updateInfo(
                req.getStreetAreaId(),
                req.getTitle(),
                req.getSummary(),
                req.getDescription(),
                req.getCoverFileId(),
                ActivityType.from(req.getActivityType()),
                req.getStartTime(),
                req.getEndTime(),
                req.getLocation(),
                req.getPoiId()
        );
    }
}
