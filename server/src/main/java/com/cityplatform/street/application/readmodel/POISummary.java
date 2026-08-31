package com.cityplatform.street.application.readmodel;

/**
 * POI 列表项查询模型。只读，精简字段。
 */
public class POISummary {

    private Long id;
    private Long streetAreaId;
    private String streetAreaName;
    private String name;
    private String poiType;
    private String status;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getStreetAreaId() { return streetAreaId; }
    public void setStreetAreaId(Long streetAreaId) { this.streetAreaId = streetAreaId; }
    public String getStreetAreaName() { return streetAreaName; }
    public void setStreetAreaName(String streetAreaName) { this.streetAreaName = streetAreaName; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPoiType() { return poiType; }
    public void setPoiType(String poiType) { this.poiType = poiType; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
