package com.cityplatform.street.controller;

import com.cityplatform.platform.web.PageParam;
import com.cityplatform.platform.web.PageResult;
import com.cityplatform.street.application.POIApplicationService;
import com.cityplatform.street.application.dto.ChangePOIStatusRequest;
import com.cityplatform.street.application.dto.CreatePOIRequest;
import com.cityplatform.street.application.dto.POIQuery;
import com.cityplatform.street.application.dto.UpdatePOIRequest;
import com.cityplatform.street.application.readmodel.POIReadModel;
import com.cityplatform.street.application.readmodel.POISummary;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * POI Controller。不编写业务逻辑，不处理 File/MultipartFile。
 */
@RestController
@RequestMapping("/api")
public class POIController {

    private final POIApplicationService service;

    public POIController(POIApplicationService service) {
        this.service = service;
    }

    @PostMapping("/street-areas/{streetAreaId}/pois")
    public POIReadModel create(@PathVariable Long streetAreaId,
                               @Valid @RequestBody CreatePOIRequest request) {
        return service.create(streetAreaId, request);
    }

    @PutMapping("/pois/{id}")
    public POIReadModel update(@PathVariable Long id,
                               @Valid @RequestBody UpdatePOIRequest request) {
        return service.update(id, request);
    }

    @GetMapping("/pois/{id}")
    public POIReadModel get(@PathVariable Long id) {
        return service.get(id);
    }

    @GetMapping("/pois")
    public PageResult<POISummary> list(
            @RequestParam(required = false) Long streetAreaId,
            @RequestParam(required = false) String poiType,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        POIQuery query = new POIQuery();
        query.setStreetAreaId(streetAreaId);
        query.setPoiType(poiType);
        query.setStatus(status);
        return service.list(query, new PageParam(page, size));
    }

    @PatchMapping("/pois/{id}/status")
    public POIReadModel changeStatus(@PathVariable Long id,
                                     @Valid @RequestBody ChangePOIStatusRequest request) {
        return service.changeStatus(id, request);
    }
}
