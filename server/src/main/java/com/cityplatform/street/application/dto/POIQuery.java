package com.cityplatform.street.application.dto;

/**
 * POI 列表查询条件。
 */
public class POIQuery {

    private Long streetAreaId;
    private String poiType;
    private String status;

    public Long getStreetAreaId() { return streetAreaId; }
    public void setStreetAreaId(Long streetAreaId) { this.streetAreaId = streetAreaId; }
    public String getPoiType() { return poiType; }
    public void setPoiType(String poiType) { this.poiType = poiType; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
