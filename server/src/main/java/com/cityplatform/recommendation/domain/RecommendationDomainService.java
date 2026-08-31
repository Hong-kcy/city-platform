package com.cityplatform.recommendation.domain;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Rule-based 推荐领域服务：只负责候选评分、排序与 Top3 选择。
 * 不查询数据库、不依赖其它业务域、不修改任何业务状态。
 *
 * 评分规则（全部可解释，无随机因素）：
 * 门店：兴趣品类匹配 +30；用餐时段且餐饮 +20；夜间且娱乐 +20；有可用优惠券 +10；距离≤500m +10 / ≤1500m +5
 * 活动：正在进行 +30；2小时内开始 +20；今天或明天开始 +10；偏好活动类型 +20；周末节庆/演出 +10
 * 路线：偏好主题匹配 +30；半日内(≤180分钟)可完成 +10
 *
 * 排序确定性：score DESC, targetId ASC；相同输入结果稳定。
 * 多样性：优先让 STORE / ACTIVITY / EXPERIENCE_ROUTE 各占一席，不足 3 张再按分数补足。
 */
public class RecommendationDomainService {

    /** 推荐卡片数量上限 */
    private static final int TOP_N = 3;

    /** 商户类型中文标签（推荐理由展示用） */
    private static final Map<String, String> MERCHANT_TYPE_LABELS = Map.of(
            "FOOD", "餐饮",
            "ENTERTAINMENT", "娱乐",
            "RETAIL", "购物",
            "SERVICE", "服务",
            "OTHER", "综合");

    /** 活动类型中文标签 */
    private static final Map<String, String> ACTIVITY_TYPE_LABELS = Map.of(
            "FESTIVAL", "节庆",
            "PERFORMANCE", "演出",
            "EXHIBITION", "展览",
            "PROMOTION", "促销",
            "CULTURE", "文化",
            "OTHER", "其他");

    /** 路线主题中文标签 */
    private static final Map<String, String> ROUTE_THEME_LABELS = Map.of(
            "FRIEND_PHOTO", "闺蜜出片",
            "SOLO_RELAX", "社恐友好",
            "FAMILY_FUN", "亲子放电",
            "SLOW_WALK", "周末慢逛",
            "OTHER", "主题");

    /**
     * 对候选集合评分、排序并选出 Top N（带类型多样性）。
     */
    public List<ScoredRecommendation> recommend(List<RecommendationCandidate> candidates,
                                                RecommendationContext context) {
        List<ScoredRecommendation> scored = new ArrayList<>();
        for (RecommendationCandidate c : candidates) {
            ScoredRecommendation s = score(c, context);
            // 无任何规则命中时给出兜底理由，保证每张卡片都可解释
            if (s.getScore() == 0) {
                s.addPoints(0, defaultReason(c.getType()));
            }
            scored.add(s);
        }
        scored.sort(Comparator
                .comparingInt(ScoredRecommendation::getScore).reversed()
                .thenComparing(s -> s.getCandidate().getTargetId()));

        return selectDiverseTopN(scored);
    }

    // ==================== 评分 ====================

    private ScoredRecommendation score(RecommendationCandidate c, RecommendationContext ctx) {
        return switch (c.getType()) {
            case STORE -> scoreStore(c, ctx);
            case ACTIVITY -> scoreActivity(c, ctx);
            case EXPERIENCE_ROUTE -> scoreRoute(c, ctx);
        };
    }

    private ScoredRecommendation scoreStore(RecommendationCandidate c, RecommendationContext ctx) {
        ScoredRecommendation s = new ScoredRecommendation(c);
        String typeLabel = label(MERCHANT_TYPE_LABELS, c.getCategoryTag());

        // 兴趣品类匹配（用户轻量画像中的商户品类标签）
        if (ctx.getGeneralInterests().contains(c.getCategoryTag())) {
            s.addPoints(30, "符合你的" + typeLabel + "兴趣");
        }

        // 时段规则：用餐时段餐饮店、夜间娱乐店
        TimePeriod period = TimePeriod.of(ctx.getNow().toLocalTime());
        if ((period == TimePeriod.LUNCH || period == TimePeriod.DINNER) && "FOOD".equals(c.getCategoryTag())) {
            s.addPoints(20, "正是用餐好时机");
        }
        if (period == TimePeriod.NIGHT && "ENTERTAINMENT".equals(c.getCategoryTag())) {
            s.addPoints(20, "夜间正合适");
        }

        // 门店附属权益：有可用优惠券（领券仍由 Coupon 域完成，推荐只提示）
        if (c.isHasCoupon()) {
            s.addPoints(10, "有可用优惠券");
        }

        // 空间接近度（仅当提供了距离锚点且门店有坐标；为空间直线接近程度，非步行距离）
        if (c.getDistanceMeters() != null) {
            if (c.getDistanceMeters() <= 500) {
                s.addPoints(10, "距离较近");
            } else if (c.getDistanceMeters() <= 1500) {
                s.addPoints(5, "就在附近");
            }
        }
        return s;
    }

    private ScoredRecommendation scoreActivity(RecommendationCandidate c, RecommendationContext ctx) {
        ScoredRecommendation s = new ScoredRecommendation(c);
        LocalDateTime now = ctx.getNow();

        // 偏好活动类型匹配
        if (ctx.getActivityInterests().contains(c.getCategoryTag())) {
            s.addPoints(20, "符合你对" + label(ACTIVITY_TYPE_LABELS, c.getCategoryTag()) + "的兴趣");
        }

        // 时间规则：进行中 > 即将开始 > 未来（已结束候选已在 Application 层排除）
        if (c.getStartTime() != null && c.getEndTime() != null) {
            if (!now.isBefore(c.getStartTime()) && !now.isAfter(c.getEndTime())) {
                s.addPoints(30, "正在进行");
            } else if (now.isBefore(c.getStartTime())) {
                Duration untilStart = Duration.between(now, c.getStartTime());
                if (!untilStart.isNegative() && untilStart.toHours() < 2) {
                    s.addPoints(20, "2小时内即将开始");
                } else if (isTodayOrTomorrow(c.getStartTime(), now)) {
                    s.addPoints(10, "即将开始");
                }
            }
        }

        // 周末规则：周末的节庆/演出活动
        if (isWeekend(now) && ("FESTIVAL".equals(c.getCategoryTag()) || "PERFORMANCE".equals(c.getCategoryTag()))) {
            s.addPoints(10, "今天是周末");
        }
        return s;
    }

    private ScoredRecommendation scoreRoute(RecommendationCandidate c, RecommendationContext ctx) {
        ScoredRecommendation s = new ScoredRecommendation(c);

        // 偏好主题匹配
        if (ctx.getExperienceInterests().contains(c.getCategoryTag())) {
            s.addPoints(30, "符合你的" + label(ROUTE_THEME_LABELS, c.getCategoryTag()) + "偏好");
        }

        // 时长规则：半日内可完成
        if (c.getEstimatedDurationMinutes() != null && c.getEstimatedDurationMinutes() <= 180) {
            s.addPoints(10, "半日即可完成");
        }
        return s;
    }

    // ==================== 选择 ====================

    /**
     * 多样性选择：第一遍按分数顺序为每种类型各取一个最高分候选；
     * 不足 Top N 时第二遍继续按分数补足（允许同类型多张）。
     */
    private List<ScoredRecommendation> selectDiverseTopN(List<ScoredRecommendation> sorted) {
        List<ScoredRecommendation> selected = new ArrayList<>();
        Set<RecommendationType> usedTypes = new HashSet<>();

        for (ScoredRecommendation s : sorted) {
            if (selected.size() >= TOP_N) {
                break;
            }
            if (usedTypes.add(s.getCandidate().getType())) {
                selected.add(s);
            }
        }
        if (selected.size() < TOP_N) {
            for (ScoredRecommendation s : sorted) {
                if (selected.size() >= TOP_N) {
                    break;
                }
                if (!selected.contains(s)) {
                    selected.add(s);
                }
            }
        }
        return selected;
    }

    // ==================== 私有辅助 ====================

    /**
     * 兜底推荐理由：候选无任何规则命中时仍给出基础解释。
     */
    private String defaultReason(RecommendationType type) {
        return switch (type) {
            case STORE -> "营业中的街区门店";
            case ACTIVITY -> "街区的近期活动";
            case EXPERIENCE_ROUTE -> "街区的主题体验路线";
        };
    }

    private boolean isTodayOrTomorrow(LocalDateTime time, LocalDateTime now) {
        return !time.isAfter(now.plusDays(1).toLocalDate().atTime(23, 59, 59));
    }

    private boolean isWeekend(LocalDateTime time) {
        switch (time.getDayOfWeek()) {
            case SATURDAY:
            case SUNDAY:
                return true;
            default:
                return false;
        }
    }

    private String label(Map<String, String> labels, String tag) {
        if (tag == null) {
            return "综合";
        }
        return labels.getOrDefault(tag, tag);
    }

    /**
     * 简单时段划分（Demo 级时间规则）：
     * 早晨 5-11 / 午餐 11-14 / 下午 14-17 / 晚餐 17-21 / 夜间 21-次日5。
     */
    enum TimePeriod {
        MORNING, LUNCH, AFTERNOON, DINNER, NIGHT;

        static TimePeriod of(LocalTime time) {
            int hour = time.getHour();
            if (hour >= 5 && hour < 11) {
                return MORNING;
            }
            if (hour >= 11 && hour < 14) {
                return LUNCH;
            }
            if (hour >= 14 && hour < 17) {
                return AFTERNOON;
            }
            if (hour >= 17 && hour < 21) {
                return DINNER;
            }
            return NIGHT;
        }
    }
}
