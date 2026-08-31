package com.cityplatform.street.application;

import com.cityplatform.street.application.dto.CreatePOIRequest;
import com.cityplatform.street.application.dto.UpdatePOIRequest;
import com.cityplatform.street.domain.POI;
import com.cityplatform.street.domain.POIType;
import org.springframework.stereotype.Component;

/**
 * POI DTO/Entity 转换器。
 */
@Component
public class POIAssembler {

    public POI toDomain(Long streetAreaId, CreatePOIRequest req) {
        return POI.create(
                streetAreaId,
                req.getName(),
                POIType.from(req.getPoiType()),
                req.getStoreId(),
                req.getLongitude(),
                req.getLatitude(),
                req.getDescription()
        );
    }

    public void applyUpdate(POI poi, UpdatePOIRequest req) {
        poi.updateInfo(
                req.getName(),
                POIType.from(req.getPoiType()),
                req.getStoreId(),
                req.getLongitude(),
                req.getLatitude(),
                req.getDescription()
        );
    }
}
