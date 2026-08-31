package com.cityplatform.route.application.readmodel;

/**
 * 路线列表项查询模型。只读，精简字段，附街区名称与 POI 数量（JOIN/子查询组装）。
 */
public class ExperienceRouteSummary {

    private Long id;
    private Long streetAreaId;
    private String streetAreaName;
    private String name;
    private String theme;
    private Integer estimatedDuration;
    private Integer poiCount;
    private String status;

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
    public Integer getEstimatedDuration() { return estimatedDuration; }
    public void setEstimatedDuration(Integer estimatedDuration) { this.estimatedDuration = estimatedDuration; }
    public Integer getPoiCount() { return poiCount; }
    public void setPoiCount(Integer poiCount) { this.poiCount = poiCount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
