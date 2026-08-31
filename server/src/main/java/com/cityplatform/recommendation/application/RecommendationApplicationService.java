package com.cityplatform.recommendation.application;

import com.cityplatform.recommendation.application.readmodel.ActivityCandidateReadModel;
import com.cityplatform.recommendation.application.readmodel.RecommendationCardReadModel;
import com.cityplatform.recommendation.application.readmodel.RouteCandidateReadModel;
import com.cityplatform.recommendation.application.readmodel.StreetAreaLocationReadModel;
import com.cityplatform.recommendation.application.readmodel.StoreCandidateReadModel;
import com.cityplatform.recommendation.application.readmodel.TodayRecommendationReadModel;
import com.cityplatform.recommendation.domain.RecommendationCandidate;
import com.cityplatform.recommendation.domain.RecommendationContext;
import com.cityplatform.recommendation.domain.RecommendationDomainService;
import com.cityplatform.recommendation.domain.RecommendationType;
import com.cityplatform.recommendation.domain.ScoredRecommendation;
import com.cityplatform.user.domain.User;
import com.cityplatform.user.domain.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * 推荐用例编排（Application 层）：
 * 1. 跨域查询候选（门店/活动/路线）并完成硬过滤（营业中、未结束、未停用）；
 * 2. 读取用户轻量画像组装 Domain 纯输入对象（RecommendationCandidate/Context）；
 * 3. 委托 RecommendationDomainService 做规则评分与 Top3 选择；
 * 4. 组装 RecommendationCard ReadModel（展示字段，不含 Domain Entity）。
 * 推荐是只读决策建议：不领取优惠券、不订阅活动、不修改任何业务状态。
 */
@Service
public class RecommendationApplicationService {

    private static final DateTimeFormatter TIME_FORMAT =
            DateTimeFormatter.ofPattern("MM-dd HH:mm", Locale.SIMPLIFIED_CHINESE);

    private final RecommendationQueryRepository queryRepository;
    private final UserRepository userRepository;
    /** 纯规则领域服务，无状态，直接持有（不引入容器依赖，保持 Domain 纯净） */
    private final RecommendationDomainService domainService = new RecommendationDomainService();

    public RecommendationApplicationService(RecommendationQueryRepository queryRepository,
                                            UserRepository userRepository) {
        this.queryRepository = queryRepository;
        this.userRepository = userRepository;
    }

    /**
     * "今日去哪"推荐。userId 可空（未登录仍可推荐，仅无个性化兴趣分）；
     * latitude/longitude 可空（未提供时使用 streetAreaId 对应街区中心作为距离锚点，
     * 两者皆无则本批次不启用距离评分）。无合适候选时返回空 cards，不抛异常。
     */
    public TodayRecommendationReadModel today(Long userId, Long streetAreaId,
                                              Double latitude, Double longitude) {
        LocalDateTime now = LocalDateTime.now();

        // 用户兴趣画像（跨域读取在 Application 层完成，Domain 不感知 User）
        Set<String> activityInterests = new HashSet<>();
        Set<String> experienceInterests = new HashSet<>();
        if (userId != null) {
            User user = userRepository.findById(userId);
            if (user != null) {
                activityInterests.addAll(parseTags(user.getPreferredActivityTypes()));
                experienceInterests.addAll(parseTags(user.getPreferredExperienceTypes()));
            }
        }
        Set<String> generalInterests = new HashSet<>(activityInterests);
        generalInterests.addAll(experienceInterests);

        // 距离锚点：用户坐标优先，其次街区中心（默认位置策略）
        Double anchorLat = latitude;
        Double anchorLng = longitude;
        if ((anchorLat == null || anchorLng == null) && streetAreaId != null) {
            StreetAreaLocationReadModel center = queryRepository.findStreetAreaCenter(streetAreaId);
            if (center != null && center.getLatitude() != null && center.getLongitude() != null) {
                anchorLat = center.getLatitude().doubleValue();
                anchorLng = center.getLongitude().doubleValue();
            }
        }

        // 组装 Domain 候选（硬过滤已在查询 SQL 中完成）
        List<StoreCandidateReadModel> storeRows = queryRepository.findStoreCandidates(streetAreaId);
        List<ActivityCandidateReadModel> activityRows = queryRepository.findActivityCandidates(streetAreaId);
        List<RouteCandidateReadModel> routeRows = queryRepository.findRouteCandidates(streetAreaId);
        List<RecommendationCandidate> candidates = new ArrayList<>();
        candidates.addAll(buildStoreCandidates(storeRows, anchorLat, anchorLng));
        candidates.addAll(buildActivityCandidates(activityRows));
        candidates.addAll(buildRouteCandidates(routeRows));

        RecommendationContext context = new RecommendationContext(
                now, activityInterests, experienceInterests, generalInterests, anchorLat, anchorLng);
        List<ScoredRecommendation> top = domainService.recommend(candidates, context);

        return new TodayRecommendationReadModel("今日去哪", buildCards(top, storeRows, activityRows, routeRows));
    }

    // ==================== 候选组装 ====================

    private List<RecommendationCandidate> buildStoreCandidates(List<StoreCandidateReadModel> rows,
                                                               Double anchorLat, Double anchorLng) {
        List<RecommendationCandidate> result = new ArrayList<>();
        for (StoreCandidateReadModel s : rows) {
            RecommendationCandidate c = new RecommendationCandidate(
                    RecommendationType.STORE, s.getId(), s.getName(), s.getMerchantType());
            c.setHasCoupon(Boolean.TRUE.equals(s.getHasCoupon()));
            c.setDistanceMeters(distanceMeters(anchorLat, anchorLng, s.getLatitude(), s.getLongitude()));
            result.add(c);
        }
        return result;
    }

    private List<RecommendationCandidate> buildActivityCandidates(List<ActivityCandidateReadModel> rows) {
        List<RecommendationCandidate> result = new ArrayList<>();
        for (ActivityCandidateReadModel a : rows) {
            RecommendationCandidate c = new RecommendationCandidate(
                    RecommendationType.ACTIVITY, a.getId(), a.getTitle(), a.getActivityType());
            c.setStartTime(a.getStartTime());
            c.setEndTime(a.getEndTime());
            result.add(c);
        }
        return result;
    }

    private List<RecommendationCandidate> buildRouteCandidates(List<RouteCandidateReadModel> rows) {
        List<RecommendationCandidate> result = new ArrayList<>();
        for (RouteCandidateReadModel r : rows) {
            RecommendationCandidate c = new RecommendationCandidate(
                    RecommendationType.EXPERIENCE_ROUTE, r.getId(), r.getName(), r.getTheme());
            c.setEstimatedDurationMinutes(r.getEstimatedDuration());
            result.add(c);
        }
        return result;
    }

    private List<RecommendationCardReadModel> buildCards(List<ScoredRecommendation> top,
                                                         List<StoreCandidateReadModel> storeRows,
                                                         List<ActivityCandidateReadModel> activityRows,
                                                         List<RouteCandidateReadModel> routeRows) {
        List<RecommendationCardReadModel> cards = new ArrayList<>();
        for (ScoredRecommendation s : top) {
            RecommendationCandidate c = s.getCandidate();
            RecommendationCardReadModel card = new RecommendationCardReadModel();
            card.setId(cards.size() + 1);
            card.setType(c.getType().name());
            card.setTargetId(c.getTargetId());
            card.setTitle(c.getTitle());
            card.setSubtitle(subtitle(c));
            card.setReason(s.getReason());
            card.setCoverImageUrl(coverUrl(c, storeRows, activityRows, routeRows));
            card.setHasCoupon(c.getType() == RecommendationType.STORE ? c.isHasCoupon() : null);
            cards.add(card);
        }
        return cards;
    }

    /**
     * 按候选类型与目标 ID 找回封面 URL（仅展示字段聚合，无封面时为 null）。
     */
    private String coverUrl(RecommendationCandidate c,
                            List<StoreCandidateReadModel> storeRows,
                            List<ActivityCandidateReadModel> activityRows,
                            List<RouteCandidateReadModel> routeRows) {
        switch (c.getType()) {
            case STORE:
                return storeRows.stream()
                        .filter(r -> r.getId().equals(c.getTargetId()))
                        .map(StoreCandidateReadModel::getCoverImageUrl)
                        .filter(java.util.Objects::nonNull)
                        .findFirst().orElse(null);
            case ACTIVITY:
                return activityRows.stream()
                        .filter(r -> r.getId().equals(c.getTargetId()))
                        .map(ActivityCandidateReadModel::getCoverImageUrl)
                        .filter(java.util.Objects::nonNull)
                        .findFirst().orElse(null);
            case EXPERIENCE_ROUTE:
                return routeRows.stream()
                        .filter(r -> r.getId().equals(c.getTargetId()))
                        .map(RouteCandidateReadModel::getCoverImageUrl)
                        .filter(java.util.Objects::nonNull)
                        .findFirst().orElse(null);
            default:
                return null;
        }
    }

    // ==================== 私有辅助 ====================

    /**
     * 卡片副标题：门店展示营业状态与距离；活动展示时间状态；路线展示预计时长。
     */
    private String subtitle(RecommendationCandidate c) {
        switch (c.getType()) {
            case STORE:
                if (c.getDistanceMeters() != null) {
                    return "现在营业中 · 距离约" + distanceText(c.getDistanceMeters());
                }
                return "现在营业中";
            case ACTIVITY:
                return activitySubtitle(c);
            case EXPERIENCE_ROUTE:
                return "约" + durationText(c.getEstimatedDurationMinutes());
            default:
                return "";
        }
    }

    private String activitySubtitle(RecommendationCandidate c) {
        LocalDateTime now = LocalDateTime.now();
        if (c.getStartTime() != null && c.getEndTime() != null
                && !now.isBefore(c.getStartTime()) && !now.isAfter(c.getEndTime())) {
            return "进行中";
        }
        if (c.getStartTime() != null) {
            return c.getStartTime().format(TIME_FORMAT) + " 开始";
        }
        return "";
    }

    private String distanceText(double meters) {
        if (meters < 1000) {
            return Math.round(meters) + "米";
        }
        return String.format(Locale.SIMPLIFIED_CHINESE, "%.1f公里", meters / 1000);
    }

    private String durationText(Integer minutes) {
        if (minutes == null || minutes <= 0) {
            return "2小时";
        }
        if (minutes < 60) {
            return minutes + "分钟";
        }
        int h = minutes / 60;
        int m = minutes % 60;
        return m == 0 ? h + "小时" : h + "小时" + m + "分钟";
    }

    /**
     * 解析逗号分隔的兴趣标签（轻量画像字段），统一大写、去空白。
     */
    private Set<String> parseTags(String csv) {
        Set<String> tags = new HashSet<>();
        if (csv == null || csv.isBlank()) {
            return tags;
        }
        Arrays.stream(csv.split("[,，\\s]+"))
                .map(String::trim)
                .filter(t -> !t.isEmpty())
                .map(t -> t.toUpperCase(Locale.ROOT))
                .forEach(tags::add);
        return tags;
    }

    /**
     * Haversine 球面距离（米）。表达"空间接近程度"，非实际步行导航距离。
     */
    private Double distanceMeters(Double anchorLat, Double anchorLng,
                                   java.math.BigDecimal lat, java.math.BigDecimal lng) {
        if (anchorLat == null || anchorLng == null || lat == null || lng == null) {
            return null;
        }
        double earthRadius = 6371000.0;
        double dLat = Math.toRadians(lat.doubleValue() - anchorLat);
        double dLng = Math.toRadians(lng.doubleValue() - anchorLng);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(anchorLat)) * Math.cos(Math.toRadians(lat.doubleValue()))
                * Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double d = 2 * earthRadius * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return Math.round(d * 10) / 10.0;
    }
}
