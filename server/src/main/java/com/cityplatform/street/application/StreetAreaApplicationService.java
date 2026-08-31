package com.cityplatform.street.application;

import com.cityplatform.platform.exception.NotFoundException;
import com.cityplatform.platform.web.PageParam;
import com.cityplatform.platform.web.PageResult;
import com.cityplatform.street.application.dto.ChangeStreetAreaStatusRequest;
import com.cityplatform.street.application.dto.CreateStreetAreaRequest;
import com.cityplatform.street.application.dto.StreetAreaQuery;
import com.cityplatform.street.application.dto.UpdateStreetAreaRequest;
import com.cityplatform.street.application.readmodel.StreetAreaReadModel;
import com.cityplatform.street.application.readmodel.StreetAreaSummary;
import com.cityplatform.street.domain.StreetArea;
import com.cityplatform.street.domain.StreetAreaRepository;
import com.cityplatform.street.domain.StreetAreaStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 街区应用服务。负责用例编排、事务管理。
 * 写操作走 Domain Repository（Entity）；查询走 QueryRepository（ReadModel）。
 */
@Service
public class StreetAreaApplicationService {

    private final StreetAreaRepository streetAreaRepository;
    private final StreetAreaQueryRepository streetAreaQueryRepository;
    private final StreetAreaAssembler assembler;

    public StreetAreaApplicationService(StreetAreaRepository streetAreaRepository,
                                         StreetAreaQueryRepository streetAreaQueryRepository,
                                         StreetAreaAssembler assembler) {
        this.streetAreaRepository = streetAreaRepository;
        this.streetAreaQueryRepository = streetAreaQueryRepository;
        this.assembler = assembler;
    }

    @Transactional
    public StreetAreaReadModel create(CreateStreetAreaRequest request) {
        StreetArea streetArea = assembler.toDomain(request);
        streetAreaRepository.insert(streetArea);
        return streetAreaQueryRepository.findById(streetArea.getId());
    }

    @Transactional
    public StreetAreaReadModel update(Long id, UpdateStreetAreaRequest request) {
        StreetArea streetArea = loadOrThrow(id);
        assembler.applyUpdate(streetArea, request);
        streetAreaRepository.update(streetArea);
        return streetAreaQueryRepository.findById(id);
    }

    public StreetAreaReadModel get(Long id) {
        StreetAreaReadModel rm = streetAreaQueryRepository.findById(id);
        if (rm == null) {
            throw new NotFoundException("街区不存在: " + id);
        }
        return rm;
    }

    public PageResult<StreetAreaSummary> list(StreetAreaQuery query, PageParam page) {
        List<StreetAreaSummary> data = streetAreaQueryRepository.findAll(query, page);
        long total = streetAreaQueryRepository.count(query);
        return new PageResult<>(data, total, page.getPage(), page.getSize());
    }

    @Transactional
    public StreetAreaReadModel changeStatus(Long id, ChangeStreetAreaStatusRequest request) {
        StreetArea streetArea = loadOrThrow(id);
        StreetAreaStatus target = StreetAreaStatus.valueOf(request.getStatus());
        if (target == StreetAreaStatus.ACTIVE) {
            streetArea.activate();
        } else {
            streetArea.deactivate();
        }
        streetAreaRepository.update(streetArea);
        return streetAreaQueryRepository.findById(id);
    }

    private StreetArea loadOrThrow(Long id) {
        StreetArea streetArea = streetAreaRepository.findById(id);
        if (streetArea == null) {
            throw new NotFoundException("街区不存在: " + id);
        }
        return streetArea;
    }
}
