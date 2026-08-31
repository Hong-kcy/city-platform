package com.cityplatform.route.domain;

import com.cityplatform.platform.exception.BusinessException;

import java.time.LocalDateTime;

/**
 * 路线 POI 关联 Entity。纯 POJO。
 * 表示"某路线中的某个 POI 及其体验顺序与推荐理由"。
 * 不校验 POI 是否存在/是否同街区（跨对象校验由 Application 层编排）。
 * route 内 sequence 唯一性由 Application 分配 + 数据库 UNIQUE 兜底。
 */
public class ExperienceRouteItem {

    private Long id;
    private Long routeId;
    private Long poiId;
    private Integer sequence;
    private String recommendationReason;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * 工厂方法：创建路线 POI 关联。sequence 从 1 开始。
     */
    public static ExperienceRouteItem create(Long routeId, Long poiId, Integer sequence,
                                             String recommendationReason) {
        if (routeId == null || poiId == null) {
            throw new BusinessException("ILLEGAL_ROUTE_ITEM", "路线POI关联必须包含路线ID与POI ID");
        }
        if (sequence == null || sequence < 1) {
            throw new BusinessException("ILLEGAL_ROUTE_ITEM", "POI顺序必须为不小于1的整数");
        }
        ExperienceRouteItem item = new ExperienceRouteItem();
        item.routeId = routeId;
        item.poiId = poiId;
        item.sequence = sequence;
        item.recommendationReason = recommendationReason;
        LocalDateTime now = LocalDateTime.now();
        item.createdAt = now;
        item.updatedAt = now;
        return item;
    }

    /**
     * 修改推荐理由。
     */
    public void updateReason(String recommendationReason) {
        this.recommendationReason = recommendationReason;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 重设顺序。由 Application 在整批调整时调用，保证 route 内无冲突。
     */
    public void reschedule(Integer sequence) {
        if (sequence == null || sequence < 1) {
            throw new BusinessException("ILLEGAL_ROUTE_ITEM", "POI顺序必须为不小于1的整数");
        }
        this.sequence = sequence;
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getRouteId() { return routeId; }
    public void setRouteId(Long routeId) { this.routeId = routeId; }
    public Long getPoiId() { return poiId; }
    public void setPoiId(Long poiId) { this.poiId = poiId; }
    public Integer getSequence() { return sequence; }
    public void setSequence(Integer sequence) { this.sequence = sequence; }
    public String getRecommendationReason() { return recommendationReason; }
    public void setRecommendationReason(String recommendationReason) { this.recommendationReason = recommendationReason; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
