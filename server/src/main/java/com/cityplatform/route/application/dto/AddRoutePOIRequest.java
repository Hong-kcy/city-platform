package com.cityplatform.route.application.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 添加 POI 到路线请求。顺序由 Application 层按"当前最大值+1"分配。
 */
public class AddRoutePOIRequest {

    @NotNull(message = "POI ID不能为空")
    private Long poiId;

    @Size(max = 255, message = "推荐理由最长255字符")
    private String recommendationReason;

    public Long getPoiId() { return poiId; }
    public void setPoiId(Long poiId) { this.poiId = poiId; }
    public String getRecommendationReason() { return recommendationReason; }
    public void setRecommendationReason(String recommendationReason) { this.recommendationReason = recommendationReason; }
}
