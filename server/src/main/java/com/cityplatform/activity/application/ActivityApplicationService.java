package com.cityplatform.activity.application;

import com.cityplatform.activity.application.dto.ActivityQuery;
import com.cityplatform.activity.application.dto.ChangeActivityStatusRequest;
import com.cityplatform.activity.application.dto.CreateActivityRequest;
import com.cityplatform.activity.application.dto.UpdateActivityRequest;
import com.cityplatform.activity.application.readmodel.ActivityDetailReadModel;
import com.cityplatform.activity.application.readmodel.ActivitySubscriptionReadModel;
import com.cityplatform.activity.application.readmodel.ActivitySummary;
import com.cityplatform.activity.application.readmodel.MyActivitySubscription;
import com.cityplatform.activity.domain.Activity;
import com.cityplatform.activity.domain.ActivityRepository;
import com.cityplatform.activity.domain.ActivityStatus;
import com.cityplatform.activity.domain.ActivitySubscription;
import com.cityplatform.activity.domain.ActivitySubscriptionRepository;
import com.cityplatform.activity.domain.SubscriptionStatus;
import com.cityplatform.platform.exception.BusinessException;
import com.cityplatform.platform.exception.IllegalStatusTransitionException;
import com.cityplatform.platform.exception.NotFoundException;
import com.cityplatform.platform.web.PageParam;
import com.cityplatform.platform.web.PageResult;
import com.cityplatform.street.domain.POIRepository;
import com.cityplatform.street.domain.StreetAreaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 活动应用服务。负责活动 CRUD、状态流转与"想去"订阅的用例编排。
 * 跨域流程由本层协调（与 Street 域交互仅通过其 Repository 接口），
 * Activity Domain 不依赖 User/Street/POI 任何外部 Domain。
 */
@Service
public class ActivityApplicationService {

    private static final Logger log = LoggerFactory.getLogger(ActivityApplicationService.class);

    private final ActivityRepository activityRepository;
    private final ActivityQueryRepository activityQueryRepository;
    private final ActivityAssembler assembler;
    private final ActivitySubscriptionRepository subscriptionRepository;
    private final ActivitySubscriptionQueryRepository subscriptionQueryRepository;
    private final StreetAreaRepository streetAreaRepository;
    private final POIRepository poiRepository;

    public ActivityApplicationService(ActivityRepository activityRepository,
                                      ActivityQueryRepository activityQueryRepository,
                                      ActivityAssembler assembler,
                                      ActivitySubscriptionRepository subscriptionRepository,
                                      ActivitySubscriptionQueryRepository subscriptionQueryRepository,
                                      StreetAreaRepository streetAreaRepository,
                                      POIRepository poiRepository) {
        this.activityRepository = activityRepository;
        this.activityQueryRepository = activityQueryRepository;
        this.assembler = assembler;
        this.subscriptionRepository = subscriptionRepository;
        this.subscriptionQueryRepository = subscriptionQueryRepository;
        this.streetAreaRepository = streetAreaRepository;
        this.poiRepository = poiRepository;
    }

    // ==================== 活动 CRUD ====================

    @Transactional
    public ActivitySummary create(CreateActivityRequest request) {
        validateStreetAreaExists(request.getStreetAreaId());
        validatePoiExistsIfPresent(request.getPoiId());
        Activity activity = assembler.toDomain(request);
        activityRepository.insert(activity);
        return activityQueryRepository.findById(activity.getId());
    }

    @Transactional
    public ActivitySummary update(Long id, UpdateActivityRequest request) {
        Activity activity = loadActivityOrThrow(id);
        validateStreetAreaExists(request.getStreetAreaId());
        validatePoiExistsIfPresent(request.getPoiId());
        assembler.applyUpdate(activity, request);
        activityRepository.update(activity);
        return activityQueryRepository.findById(id);
    }

    public ActivitySummary get(Long id) {
        ActivitySummary rm = activityQueryRepository.findById(id);
        if (rm == null) {
            throw new NotFoundException("活动不存在: " + id);
        }
        return rm;
    }

    /**
     * 活动详情（跨域聚合视图）。携带有效登录态时附带当前用户订阅状态；
     * 匿名访问 subscriptionStatus 为 null。
     * 用户公开视图（management=false）仅返回已发布(PUBLISHED)活动，
     * 未发布/已下线按不存在处理；商户/运营管理视图（management=true）
     * 可查看全部状态，待商户端鉴权接入后收紧为登录态校验。
     */
    public ActivityDetailReadModel getDetail(Long id, Long userId, boolean management) {
        ActivityDetailReadModel rm = activityQueryRepository.findDetailById(id);
        if (rm == null) {
            throw new NotFoundException("活动不存在: " + id);
        }
        if (!management && !ActivityStatus.PUBLISHED.name().equals(rm.getStatus())) {
            throw new NotFoundException("活动不存在: " + id);
        }
        if (userId != null) {
            ActivitySubscriptionReadModel sub =
                    subscriptionQueryRepository.findByUserIdAndActivityId(userId, id);
            rm.setSubscriptionStatus(sub == null ? null : sub.getStatus());
        }
        return rm;
    }

    public PageResult<ActivitySummary> list(ActivityQuery query, PageParam page) {
        List<ActivitySummary> data = activityQueryRepository.findAll(query, page);
        long total = activityQueryRepository.count(query);
        return new PageResult<>(data, total, page.getPage(), page.getSize());
    }

    /**
     * 状态变更。合法流转仅 DRAFT -> PUBLISHED 与 PUBLISHED -> OFFLINE，
     * 由 Activity Entity 维护规则，非法跃迁抛 IllegalStatusTransitionException。
     */
    @Transactional
    public ActivitySummary changeStatus(Long id, ChangeActivityStatusRequest request) {
        Activity activity = loadActivityOrThrow(id);
        ActivityStatus target = ActivityStatus.valueOf(request.getStatus());
        if (target == ActivityStatus.PUBLISHED) {
            activity.publish();
        } else if (target == ActivityStatus.OFFLINE) {
            activity.takeOffline();
        } else {
            throw new IllegalStatusTransitionException("不支持流转为状态: " + target);
        }
        activityRepository.update(activity);
        return activityQueryRepository.findById(id);
    }

    // ==================== 用户"想去"订阅 ====================

    /**
     * 订阅活动（点击"想去"）。userId 一律来自平台登录态。
     * 规则：仅已发布(PUBLISHED)活动可订阅；重复订阅幂等返回已有记录；
     * 取消过的订阅复用原记录重新生效（数据库 UNIQUE 兜底防重复数据）。
     */
    @Transactional
    public ActivitySubscriptionReadModel subscribe(Long activityId, Long userId) {
        Activity activity = loadActivityOrThrow(activityId);
        if (!activity.subscribable()) {
            throw new BusinessException("ACTIVITY_NOT_SUBSCRIBABLE",
                    "活动当前状态不可订阅，仅已发布活动可订阅");
        }
        ActivitySubscription existing =
                subscriptionRepository.findByUserIdAndActivityId(userId, activityId);
        if (existing == null) {
            ActivitySubscription subscription = ActivitySubscription.subscribe(activityId, userId);
            subscriptionRepository.insert(subscription);
            log.info("用户订阅活动: userId={}, activityId={}", userId, activityId);
        } else if (existing.getStatus() == SubscriptionStatus.CANCELLED) {
            existing.reactivate();
            subscriptionRepository.update(existing);
            log.info("用户重新订阅活动: userId={}, activityId={}", userId, activityId);
        }
        return getSubscriptionStatus(activityId, userId);
    }

    /**
     * 取消订阅（幂等）。未订阅过的活动返回 404；
     * A 用户无法触碰 B 用户的订阅（查询条件含 userId）。
     */
    @Transactional
    public ActivitySubscriptionReadModel cancelSubscription(Long activityId, Long userId) {
        ActivitySubscription existing =
                subscriptionRepository.findByUserIdAndActivityId(userId, activityId);
        if (existing == null) {
            throw new NotFoundException("未订阅该活动: " + activityId);
        }
        if (existing.getStatus() == SubscriptionStatus.ACTIVE) {
            existing.cancel();
            subscriptionRepository.update(existing);
        }
        return getSubscriptionStatus(activityId, userId);
    }

    /**
     * 查询当前用户对某活动的订阅状态。未订阅时返回 status=null 的空模型而非 404，
     * 便于前端直接渲染"想去"按钮初始态。
     */
    public ActivitySubscriptionReadModel getSubscriptionStatus(Long activityId, Long userId) {
        ActivitySubscriptionReadModel rm =
                subscriptionQueryRepository.findByUserIdAndActivityId(userId, activityId);
        if (rm == null) {
            return new ActivitySubscriptionReadModel(activityId, userId);
        }
        return rm;
    }

    /**
     * 我的活动（生效订阅列表）。含已下线活动，由前端依据 activityStatus 展示。
     */
    public PageResult<MyActivitySubscription> mySubscriptions(Long userId, PageParam page) {
        List<MyActivitySubscription> data = subscriptionQueryRepository.findMySubscriptions(userId, page);
        long total = subscriptionQueryRepository.countMySubscriptions(userId);
        return new PageResult<>(data, total, page.getPage(), page.getSize());
    }

    // ==================== 私有辅助 ====================

    private Activity loadActivityOrThrow(Long id) {
        Activity activity = activityRepository.findById(id);
        if (activity == null) {
            throw new NotFoundException("活动不存在: " + id);
        }
        return activity;
    }

    /**
     * 跨域校验街区存在（Application 层协调，与 POI 创建校验模式一致）。
     */
    private void validateStreetAreaExists(Long streetAreaId) {
        if (streetAreaRepository.findById(streetAreaId) == null) {
            throw new NotFoundException("街区不存在: " + streetAreaId);
        }
    }

    /**
     * poiId 为可选弱关联，提供时校验存在性。
     */
    private void validatePoiExistsIfPresent(Long poiId) {
        if (poiId != null && poiRepository.findById(poiId) == null) {
            throw new NotFoundException("POI不存在: " + poiId);
        }
    }
}
