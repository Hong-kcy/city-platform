package com.cityplatform.route.application.readmodel;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 路线详情查询模型。跨域聚合视图（Query 层 JOIN 组装）：
 * 路线本体 + 街区名称 + 按 sequence 排序的完整 POI 列表（含 Store 展示信息）。
 * 一次查询返回完整列表，避免前端 N+1 请求。
 * Street/Merchant Entity 不进入 Route Domain，聚合只发生在查询层。
 */
public class ExperienceRouteDetailReadModel {

    private Long id;
    private Long streetAreaId;
    private String streetAreaName;
    private String name;
    private String theme;
    private String description;
    private Integer estimatedDuration;
    private Integer poiCount;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<RoutePOIReadModel> items;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getStreetAreaId() { return streetAreaId; }
    public void setStreetAreaId(Long streetAreaId) { this.streetAreaId = streetAreaId; }
    public String getStreetAreaName() { return streetAreaName; }
    public void setStreetAreaName(String streetAreaName) { this.streetAreaName = streetAreaName; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getTheme() { return theme; }
    public void setTheme(String theme) { this.theme = theme; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Integer getEstimatedDuration() { return estimatedDuration; }
    public void setEstimatedDuration(Integer estimatedDuration) { this.estimatedDuration = estimatedDuration; }
    public Integer getPoiCount() { return poiCount; }
    public void setPoiCount(Integer poiCount) { this.poiCount = poiCount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public List<RoutePOIReadModel> getItems() { return items; }
    public void setItems(List<RoutePOIReadModel> items) { this.items = items; }
}
