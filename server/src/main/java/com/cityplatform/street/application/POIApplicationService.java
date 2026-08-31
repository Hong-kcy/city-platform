package com.cityplatform.street.application;

import com.cityplatform.platform.exception.NotFoundException;
import com.cityplatform.platform.web.PageParam;
import com.cityplatform.platform.web.PageResult;
import com.cityplatform.street.application.dto.ChangePOIStatusRequest;
import com.cityplatform.street.application.dto.CreatePOIRequest;
import com.cityplatform.street.application.dto.POIQuery;
import com.cityplatform.street.application.dto.UpdatePOIRequest;
import com.cityplatform.street.application.readmodel.POIReadModel;
import com.cityplatform.street.application.readmodel.POISummary;
import com.cityplatform.street.domain.POI;
import com.cityplatform.street.domain.POIRepository;
import com.cityplatform.street.domain.POIStatus;
import com.cityplatform.street.domain.StreetAreaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * POI 应用服务。创建 POI 前校验所属街区存在（跨实体校验，由 Application 协调）。
 * 不校验 storeId 是否对应真实 Store（POI 与 Store 是弱关联）。
 * POI 的不变量（poiType 与 storeId 一致性）由 POI Entity 自身保证。
 */
@Service
public class POIApplicationService {

    private final POIRepository poiRepository;
    private final POIQueryRepository poiQueryRepository;
    private final POIAssembler assembler;
    private final StreetAreaRepository streetAreaRepository;

    public POIApplicationService(POIRepository poiRepository,
                                  POIQueryRepository poiQueryRepository,
                                  POIAssembler assembler,
                                  StreetAreaRepository streetAreaRepository) {
        this.poiRepository = poiRepository;
        this.poiQueryRepository = poiQueryRepository;
        this.assembler = assembler;
        this.streetAreaRepository = streetAreaRepository;
    }

    @Transactional
    public POIReadModel create(Long streetAreaId, CreatePOIRequest request) {
        if (streetAreaRepository.findById(streetAreaId) == null) {
            throw new NotFoundException("街区不存在: " + streetAreaId);
        }
        POI poi = assembler.toDomain(streetAreaId, request);
        poiRepository.insert(poi);
        return poiQueryRepository.findById(poi.getId());
    }

    @Transactional
    public POIReadModel update(Long id, UpdatePOIRequest request) {
        POI poi = loadOrThrow(id);
        assembler.applyUpdate(poi, request);
        poiRepository.update(poi);
        return poiQueryRepository.findById(id);
    }

    public POIReadModel get(Long id) {
        POIReadModel rm = poiQueryRepository.findById(id);
        if (rm == null) {
            throw new NotFoundException("POI不存在: " + id);
        }
        return rm;
    }

    public PageResult<POISummary> list(POIQuery query, PageParam page) {
        List<POISummary> data = poiQueryRepository.findAll(query, page);
        long total = poiQueryRepository.count(query);
        return new PageResult<>(data, total, page.getPage(), page.getSize());
    }

    @Transactional
    public POIReadModel changeStatus(Long id, ChangePOIStatusRequest request) {
        POI poi = loadOrThrow(id);
        POIStatus target = POIStatus.valueOf(request.getStatus());
        if (target == POIStatus.ACTIVE) {
            poi.activate();
        } else {
            poi.deactivate();
        }
        poiRepository.update(poi);
        return poiQueryRepository.findById(id);
    }

    private POI loadOrThrow(Long id) {
        POI poi = poiRepository.findById(id);
        if (poi == null) {
            throw new NotFoundException("POI不存在: " + id);
        }
        return poi;
    }
}
