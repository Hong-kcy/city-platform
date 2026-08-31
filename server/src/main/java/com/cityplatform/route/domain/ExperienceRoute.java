package com.cityplatform.route.domain;

import com.cityplatform.platform.exception.BusinessException;
import com.cityplatform.platform.exception.IllegalStatusTransitionException;

import java.time.LocalDateTime;

/**
 * 主题体验路线 Entity。纯 POJO。
 * 只回答"去哪些地方、按什么顺序、为什么"，不负责导航/定位/道路网络。
 * streetAreaId 不可变：路线强归属单个街区，防止跨街区随意拼接 POI。
 * POI 合法性（存在/状态/同街区）由 Application 层跨实体校验。
 */
public class ExperienceRoute {

    private Long id;
    private Long streetAreaId;
    private String name;
    private RouteTheme theme;
    private String description;
    private Integer estimatedDuration;
    private RouteStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * 工厂方法：创建路线。默认 ACTIVE。
     */
    public static ExperienceRoute create(Long streetAreaId, String name, RouteTheme theme,
                                         String description, Integer estimatedDuration) {
        validateDuration(estimatedDuration);
        ExperienceRoute r = new ExperienceRoute();
        r.streetAreaId = streetAreaId;
        r.name = name;
        r.theme = theme;
        r.description = description;
        r.estimatedDuration = estimatedDuration;
        r.status = RouteStatus.ACTIVE;
        LocalDateTime now = LocalDateTime.now();
        r.createdAt = now;
        r.updatedAt = now;
        return r;
    }

    /**
     * 修改基础信息（PUT 全量替换语义）。streetAreaId 不可变。
     */
    public void updateInfo(String name, RouteTheme theme, String description, Integer estimatedDuration) {
        validateDuration(estimatedDuration);
        this.name = name;
        this.theme = theme;
        this.description = description;
        this.estimatedDuration = estimatedDuration;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 路线不变量：预计时长必须为正整数（分钟）。
     */
    private static void validateDuration(Integer estimatedDuration) {
        if (estimatedDuration == null || estimatedDuration < 1) {
            throw new BusinessException("ILLEGAL_ROUTE_DURATION", "预计时长必须为正整数分钟数");
        }
    }

    public void activate() {
        if (this.status == RouteStatus.ACTIVE) {
            throw new IllegalStatusTransitionException("路线已是启用状态");
        }
        this.status = RouteStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

    public void deactivate() {
        if (this.status == RouteStatus.INACTIVE) {
            throw new IllegalStatusTransitionException("路线已是停用状态");
        }
        this.status = RouteStatus.INACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 用户端可见：仅 ACTIVE 路线对用户展示。
     */
    public boolean visibleToUser() {
        return this.status == RouteStatus.ACTIVE;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getStreetAreaId() { return streetAreaId; }
    public void setStreetAreaId(Long streetAreaId) { this.streetAreaId = streetAreaId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public RouteTheme getTheme() { return theme; }
    public void setTheme(RouteTheme theme) { this.theme = theme; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Integer getEstimatedDuration() { return estimatedDuration; }
    public void setEstimatedDuration(Integer estimatedDuration) { this.estimatedDuration = estimatedDuration; }
    public RouteStatus getStatus() { return status; }
    public void setStatus(RouteStatus status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
