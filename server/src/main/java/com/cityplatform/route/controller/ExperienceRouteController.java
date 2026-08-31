package com.cityplatform.route.controller;

import com.cityplatform.platform.web.PageParam;
import com.cityplatform.platform.web.PageResult;
import com.cityplatform.route.application.ExperienceRouteApplicationService;
import com.cityplatform.route.application.dto.AddRoutePOIRequest;
import com.cityplatform.route.application.dto.ChangeExperienceRouteStatusRequest;
import com.cityplatform.route.application.dto.CreateExperienceRouteRequest;
import com.cityplatform.route.application.dto.ExperienceRouteQuery;
import com.cityplatform.route.application.dto.ReorderRoutePOIsRequest;
import com.cityplatform.route.application.dto.UpdateExperienceRouteRequest;
import com.cityplatform.route.application.readmodel.ExperienceRouteDetailReadModel;
import com.cityplatform.route.application.readmodel.ExperienceRouteSummary;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
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
 * 主题体验路线 Controller。仅负责接入 HTTP、参数校验、调用 ApplicationService。
 * 列表/详情匿名可访问；用户端查询约定携带 status=ACTIVE。
 */
@RestController
@RequestMapping("/api/experience-routes")
public class ExperienceRouteController {

    private final ExperienceRouteApplicationService service;

    public ExperienceRouteController(ExperienceRouteApplicationService service) {
        this.service = service;
    }

    @PostMapping
    public ExperienceRouteSummary create(@Valid @RequestBody CreateExperienceRouteRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public ExperienceRouteSummary update(@PathVariable Long id,
                                         @Valid @RequestBody UpdateExperienceRouteRequest request) {
        return service.update(id, request);
    }

    /**
     * 路线详情。一次返回路线本体 + 按 sequence 排序的完整 POI 列表（含 Store 展示信息）。
     * 默认为用户公开视图，仅返回启用(ACTIVE)路线；
     * 商户/运营管理端携带 management=true 查看全部状态，待鉴权接入后收紧。
     */
    @GetMapping("/{id}")
    public ExperienceRouteDetailReadModel getDetail(
            @PathVariable Long id,
            @RequestParam(defaultValue = "false") boolean management) {
        return service.getDetail(id, management);
    }

    @GetMapping
    public PageResult<ExperienceRouteSummary> list(
            @RequestParam(required = false) Long streetAreaId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String theme,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        ExperienceRouteQuery query = new ExperienceRouteQuery();
        query.setStreetAreaId(streetAreaId);
        query.setName(name);
        query.setTheme(theme);
        query.setStatus(status);
        return service.list(query, new PageParam(page, size));
    }

    @PatchMapping("/{id}/status")
    public ExperienceRouteSummary changeStatus(@PathVariable Long id,
                                               @Valid @RequestBody ChangeExperienceRouteStatusRequest request) {
        return service.changeStatus(id, request);
    }

    @PostMapping("/{routeId}/pois")
    public ExperienceRouteDetailReadModel addPOI(@PathVariable Long routeId,
                                                 @Valid @RequestBody AddRoutePOIRequest request) {
        return service.addPOI(routeId, request);
    }

    @DeleteMapping("/{routeId}/pois/{poiId}")
    public ExperienceRouteDetailReadModel removePOI(@PathVariable Long routeId,
                                                    @PathVariable Long poiId) {
        return service.removePOI(routeId, poiId);
    }

    @PutMapping("/{routeId}/pois/order")
    public ExperienceRouteDetailReadModel reorderPOIs(@PathVariable Long routeId,
                                                      @Valid @RequestBody ReorderRoutePOIsRequest request) {
        return service.reorderPOIs(routeId, request);
    }
}
