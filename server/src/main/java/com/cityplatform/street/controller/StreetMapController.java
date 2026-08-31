package com.cityplatform.street.controller;

import com.cityplatform.street.application.StreetMapApplicationService;
import com.cityplatform.street.application.readmodel.StreetMapReadModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 街区地图展示 Controller。面向用户端地图首页。
 * 仅提供聚合查询，不含管理操作。
 */
@RestController
@RequestMapping("/api/street-areas")
public class StreetMapController {

    private final StreetMapApplicationService service;

    public StreetMapController(StreetMapApplicationService service) {
        this.service = service;
    }

    @GetMapping("/{id}/map")
    public StreetMapReadModel getMapData(@PathVariable Long id) {
        return service.getMapData(id);
    }
}
