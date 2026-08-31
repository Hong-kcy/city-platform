package com.cityplatform.street.application;

import com.cityplatform.street.application.dto.CreateStreetAreaRequest;
import com.cityplatform.street.application.dto.UpdateStreetAreaRequest;
import com.cityplatform.street.domain.StreetArea;
import org.springframework.stereotype.Component;

/**
 * 街区 DTO/Entity 转换器。规范：DTO 转换统一放 Assembler。
 */
@Component
public class StreetAreaAssembler {

    public StreetArea toDomain(CreateStreetAreaRequest req) {
        return StreetArea.create(
                req.getName(),
                req.getIntroduction(),
                req.getCoverImageFileId(),
                req.getLongitude(),
                req.getLatitude()
        );
    }

    public void applyUpdate(StreetArea streetArea, UpdateStreetAreaRequest req) {
        streetArea.updateInfo(
                req.getName(),
                req.getIntroduction(),
                req.getCoverImageFileId(),
                req.getLongitude(),
                req.getLatitude()
        );
    }
}
