package com.cityplatform.route.application.dto;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

/**
 * 调整路线 POI 顺序请求。poiIds 必须为该路线当前全部 POI 的一个排列。
 * Application 层按下标转换为 sequence: 1..n。
 */
public class ReorderRoutePOIsRequest {

    @NotEmpty(message = "POI顺序列表不能为空")
    private List<Long> poiIds;

    public List<Long> getPoiIds() { return poiIds; }
    public void setPoiIds(List<Long> poiIds) { this.poiIds = poiIds; }
}
