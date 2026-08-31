package com.cityplatform.street.application;

import com.cityplatform.platform.exception.NotFoundException;
import com.cityplatform.street.application.readmodel.StreetMapReadModel;
import org.springframework.stereotype.Service;

/**
 * 街区地图展示应用服务。面向用户端地图首页查询。
 * 纯读操作，无事务，仅委托 QueryRepository 并处理 NotFound。
 */
@Service
public class StreetMapApplicationService {

    private final StreetMapQueryRepository streetMapQueryRepository;

    public StreetMapApplicationService(StreetMapQueryRepository streetMapQueryRepository) {
        this.streetMapQueryRepository = streetMapQueryRepository;
    }

    public StreetMapReadModel getMapData(Long streetAreaId) {
        StreetMapReadModel result = streetMapQueryRepository.findMapData(streetAreaId);
        if (result == null) {
            throw new NotFoundException("街区不存在: " + streetAreaId);
        }
        return result;
    }
}
