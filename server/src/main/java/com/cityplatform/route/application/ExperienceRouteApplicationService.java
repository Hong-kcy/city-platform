package com.cityplatform.route.application;

import com.cityplatform.platform.exception.BusinessException;
import com.cityplatform.platform.exception.IllegalStatusTransitionException;
import com.cityplatform.platform.exception.NotFoundException;
import com.cityplatform.platform.web.PageParam;
import com.cityplatform.platform.web.PageResult;
import com.cityplatform.route.application.dto.AddRoutePOIRequest;
import com.cityplatform.route.application.dto.ChangeExperienceRouteStatusRequest;
import com.cityplatform.route.application.dto.CreateExperienceRouteRequest;
import com.cityplatform.route.application.dto.ExperienceRouteQuery;
import com.cityplatform.route.application.dto.ReorderRoutePOIsRequest;
import com.cityplatform.route.application.dto.UpdateExperienceRouteRequest;
import com.cityplatform.route.application.readmodel.ExperienceRouteDetailReadModel;
import com.cityplatform.route.application.readmodel.ExperienceRouteSummary;
import com.cityplatform.route.domain.ExperienceRoute;
import com.cityplatform.route.domain.ExperienceRouteItem;
import com.cityplatform.route.domain.ExperienceRouteItemRepository;
import com.cityplatform.route.domain.ExperienceRouteRepository;
import com.cityplatform.route.domain.RouteStatus;
import com.cityplatform.street.domain.POI;
import com.cityplatform.street.domain.POIRepository;
import com.cityplatform.street.domain.POIStatus;
import com.cityplatform.street.domain.StreetAreaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 主题体验路线应用服务。负责路线 CRUD、状态流转与路线 POI 的
 * 添加/删除/顺序调整用例编排。事务边界在本层。
 * 跨域校验（POI 存在/状态/同街区）由本层协调，仅通过 Street 域 Repository 接口，
 * Route Domain 不依赖 Street/Merchant 任何外部 Domain。
 */
@Service
public class ExperienceRouteApplicationService {

    private static final Logger log = LoggerFactory.getLogger(ExperienceRouteApplicationService.class);

    /** 顺序整体调整前的临时偏移量，规避 UNIQUE(route_id, sequence) 中间冲突 */
    private static final int SEQUENCE_SHIFT_OFFSET = 1_000_000;

    private final ExperienceRouteRepository routeRepository;
    private final ExperienceRouteItemRepository itemRepository;
    private final ExperienceRouteQueryRepository queryRepository;
    private final ExperienceRouteAssembler assembler;
    private final StreetAreaRepository streetAreaRepository;
    private final POIRepository poiRepository;

    public ExperienceRouteApplicationService(ExperienceRouteRepository routeRepository,
                                             ExperienceRouteItemRepository itemRepository,
                                             ExperienceRouteQueryRepository queryRepository,
                                             ExperienceRouteAssembler assembler,
                                             StreetAreaRepository streetAreaRepository,
                                             POIRepository poiRepository) {
        this.routeRepository = routeRepository;
        this.itemRepository = itemRepository;
        this.queryRepository = queryRepository;
        this.assembler = assembler;
        this.streetAreaRepository = streetAreaRepository;
        this.poiRepository = poiRepository;
    }

    // ==================== 路线 CRUD ====================

    @Transactional
    public ExperienceRouteSummary create(CreateExperienceRouteRequest request) {
        validateStreetAreaExists(request.getStreetAreaId());
        ExperienceRoute route = assembler.toDomain(request);
        routeRepository.insert(route);
        return queryRepository.findSummaryById(route.getId());
    }

    @Transactional
    public ExperienceRouteSummary update(Long id, UpdateExperienceRouteRequest request) {
        ExperienceRoute route = loadRouteOrThrow(id);
        assembler.applyUpdate(route, request);
        routeRepository.update(route);
        return queryRepository.findSummaryById(id);
    }

    /**
     * 路线详情（跨域聚合视图）。一次返回按 sequence 排序的完整 POI 列表。
     * 管理视图（内部管理操作后回显使用）。
     */
    public ExperienceRouteDetailReadModel getDetail(Long id) {
        return getDetail(id, true);
    }

    /**
     * 路线详情。用户公开视图（management=false）仅返回启用(ACTIVE)路线，
     * 已停用按不存在处理；管理视图（management=true）可查看全部状态，
     * 待商户/运营端鉴权接入后收紧为登录态校验。
     */
    public ExperienceRouteDetailReadModel getDetail(Long id, boolean management) {
        ExperienceRouteDetailReadModel rm = queryRepository.findDetailById(id);
        if (rm == null) {
            throw new NotFoundException("路线不存在: " + id);
        }
        if (!management && !RouteStatus.ACTIVE.name().equals(rm.getStatus())) {
            throw new NotFoundException("路线不存在: " + id);
        }
        return rm;
    }

    public PageResult<ExperienceRouteSummary> list(ExperienceRouteQuery query, PageParam page) {
        List<ExperienceRouteSummary> data = queryRepository.findAll(query, page);
        long total = queryRepository.count(query);
        return new PageResult<>(data, total, page.getPage(), page.getSize());
    }

    /**
     * 状态变更。合法流转 ACTIVE <-> INACTIVE，由 Entity 维护规则，
     * 非法跃迁抛 IllegalStatusTransitionException。
     */
    @Transactional
    public ExperienceRouteSummary changeStatus(Long id, ChangeExperienceRouteStatusRequest request) {
        ExperienceRoute route = loadRouteOrThrow(id);
        RouteStatus target = RouteStatus.valueOf(request.getStatus());
        if (target == RouteStatus.ACTIVE) {
            route.activate();
        } else {
            route.deactivate();
        }
        routeRepository.update(route);
        return queryRepository.findSummaryById(id);
    }

    // ==================== 路线 POI 管理 ====================

    /**
     * 添加 POI 到路线。顺序自动分配为当前最大 sequence + 1。
     * 校验链：路线存在 -> POI 存在 -> POI 状态可用 -> POI 与路线同街区 -> 未重复添加。
     */
    @Transactional
    public ExperienceRouteDetailReadModel addPOI(Long routeId, AddRoutePOIRequest request) {
        ExperienceRoute route = loadRouteOrThrow(routeId);
        POI poi = poiRepository.findById(request.getPoiId());
        if (poi == null) {
            throw new NotFoundException("POI不存在: " + request.getPoiId());
        }
        if (poi.getStatus() != POIStatus.ACTIVE) {
            throw new BusinessException("POI_NOT_USABLE", "POI已停用，不能加入路线");
        }
        if (!poi.getStreetAreaId().equals(route.getStreetAreaId())) {
            throw new BusinessException("POI_AREA_MISMATCH", "POI与路线不属于同一街区，不能加入");
        }
        if (itemRepository.findByRouteIdAndPoiId(routeId, request.getPoiId()) != null) {
            throw new BusinessException("POI_ALREADY_IN_ROUTE", "该POI已在路线中，不能重复添加");
        }
        int nextSequence = itemRepository.findByRouteIdOrderBySequence(routeId).size() + 1;
        ExperienceRouteItem item = ExperienceRouteItem.create(
                routeId, request.getPoiId(), nextSequence, request.getRecommendationReason());
        itemRepository.insert(item);
        log.info("POI加入路线: routeId={}, poiId={}, sequence={}", routeId, request.getPoiId(), nextSequence);
        return getDetail(routeId);
    }

    /**
     * 从路线移除 POI（关联行为物理删除，路线本体仍为软删除模式）。
     * 删除后自动压缩 sequence 保持 1..n 连续。
     */
    @Transactional
    public ExperienceRouteDetailReadModel removePOI(Long routeId, Long poiId) {
        loadRouteOrThrow(routeId);
        ExperienceRouteItem item = itemRepository.findByRouteIdAndPoiId(routeId, poiId);
        if (item == null) {
            throw new NotFoundException("该POI不在路线中: routeId=" + routeId + ", poiId=" + poiId);
        }
        itemRepository.deleteById(item.getId());
        compactSequence(routeId);
        log.info("POI移出路线: routeId={}, poiId={}", routeId, poiId);
        return getDetail(routeId);
    }

    /**
     * 批量调整路线 POI 顺序。poiIds 必须为当前全部 POI 的一个排列（无重复、无缺失）。
     * 更新策略：同一事务内先整体偏移 sequence 避开唯一键区间，再写入终值 1..n。
     */
    @Transactional
    public ExperienceRouteDetailReadModel reorderPOIs(Long routeId, ReorderRoutePOIsRequest request) {
        loadRouteOrThrow(routeId);
        List<ExperienceRouteItem> items = itemRepository.findByRouteIdOrderBySequence(routeId);
        List<Long> poiIds = request.getPoiIds();
        validateReorderPayload(items, poiIds);

        // 阶段一：整体偏移到临时区间，腾出 1..n 终值空间
        itemRepository.shiftSequence(routeId, SEQUENCE_SHIFT_OFFSET);
        // 阶段二：按目标顺序写入终值
        for (int i = 0; i < poiIds.size(); i++) {
            Long poiId = poiIds.get(i);
            ExperienceRouteItem item = items.stream()
                    .filter(it -> it.getPoiId().equals(poiId))
                    .findFirst()
                    .orElseThrow(() -> new NotFoundException("POI不存在: " + poiId));
            item.reschedule(i + 1);
            itemRepository.update(item);
        }
        log.info("路线POI顺序调整: routeId={}, 新顺序={}", routeId, poiIds);
        return getDetail(routeId);
    }

    // ==================== 私有辅助 ====================

    private ExperienceRoute loadRouteOrThrow(Long id) {
        ExperienceRoute route = routeRepository.findById(id);
        if (route == null) {
            throw new NotFoundException("路线不存在: " + id);
        }
        return route;
    }

    /**
     * 跨域校验街区存在（Application 层协调，与 Activity 创建校验模式一致）。
     */
    private void validateStreetAreaExists(Long streetAreaId) {
        if (streetAreaRepository.findById(streetAreaId) == null) {
            throw new NotFoundException("街区不存在: " + streetAreaId);
        }
    }

    /**
     * 排序请求合法性：与现有 items 的 POI 集合完全一致（不缺失、不多余、无重复）。
     */
    private void validateReorderPayload(List<ExperienceRouteItem> items, List<Long> poiIds) {
        Set<Long> existing = new HashSet<>();
        items.forEach(it -> existing.add(it.getPoiId()));
        Set<Long> requested = new HashSet<>(poiIds);
        if (requested.size() != poiIds.size()) {
            throw new BusinessException("ILLEGAL_REORDER", "排序请求包含重复POI");
        }
        if (!requested.equals(existing)) {
            throw new BusinessException("ILLEGAL_REORDER", "排序请求必须且只能包含路线当前全部POI");
        }
    }

    /**
     * 删除 POI 后压缩 sequence，保持 1..n 连续。
     * 同样采用偏移策略避免唯一键中间冲突。
     */
    private void compactSequence(Long routeId) {
        List<ExperienceRouteItem> items = itemRepository.findByRouteIdOrderBySequence(routeId);
        if (items.isEmpty()) {
            return;
        }
        itemRepository.shiftSequence(routeId, SEQUENCE_SHIFT_OFFSET);
        for (int i = 0; i < items.size(); i++) {
            ExperienceRouteItem item = items.get(i);
            item.reschedule(i + 1);
            itemRepository.update(item);
        }
    }
}
