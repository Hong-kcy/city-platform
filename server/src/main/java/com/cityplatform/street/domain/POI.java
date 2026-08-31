package com.cityplatform.street.domain;

import com.cityplatform.platform.exception.BusinessException;
import com.cityplatform.platform.exception.IllegalStatusTransitionException;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * POI Entity。纯 POJO。
 * POI 与 Store 是弱关联：storeId 仅在 poiType=STORE 时非空，且不校验 Store 是否真实存在。
 * POI 的 status 与所属 StreetArea 的 status 独立，各自维护生命周期。
 */
public class POI {

    private Long id;
    private Long streetAreaId;
    private String name;
    private POIType poiType;
    private Long storeId;
    private BigDecimal longitude;
    private BigDecimal latitude;
    private String description;
    private POIStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * 工厂方法：创建 POI。默认 ACTIVE。
     * 校验 poiType 与 storeId 的一致性（POI 自身不变量，非跨域校验）。
     */
    public static POI create(Long streetAreaId, String name, POIType poiType, Long storeId,
                             BigDecimal longitude, BigDecimal latitude, String description) {
        validateStoreRef(poiType, storeId);
        POI p = new POI();
        p.streetAreaId = streetAreaId;
        p.name = name;
        p.poiType = poiType;
        p.storeId = storeId;
        p.longitude = longitude;
        p.latitude = latitude;
        p.description = description;
        p.status = POIStatus.ACTIVE;
        LocalDateTime now = LocalDateTime.now();
        p.createdAt = now;
        p.updatedAt = now;
        return p;
    }

    /**
     * 修改基础信息（PUT 全量替换语义）。streetAreaId 不可变。
     */
    public void updateInfo(String name, POIType poiType, Long storeId,
                           BigDecimal longitude, BigDecimal latitude, String description) {
        validateStoreRef(poiType, storeId);
        this.name = name;
        this.poiType = poiType;
        this.storeId = storeId;
        this.longitude = longitude;
        this.latitude = latitude;
        this.description = description;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * POI 不变量：STORE 类型必须关联 storeId，非 STORE 类型 storeId 必须为空。
     */
    private static void validateStoreRef(POIType poiType, Long storeId) {
        if (poiType == POIType.STORE && storeId == null) {
            throw new BusinessException("ILLEGAL_POI_STORE_REF", "STORE类型POI必须关联门店");
        }
        if (poiType != POIType.STORE && storeId != null) {
            throw new BusinessException("ILLEGAL_POI_STORE_REF", "非STORE类型POI不得关联门店");
        }
    }

    public void activate() {
        if (this.status == POIStatus.ACTIVE) {
            throw new IllegalStatusTransitionException("POI已是启用状态");
        }
        this.status = POIStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

    public void deactivate() {
        if (this.status == POIStatus.INACTIVE) {
            throw new IllegalStatusTransitionException("POI已是停用状态");
        }
        this.status = POIStatus.INACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getStreetAreaId() { return streetAreaId; }
    public void setStreetAreaId(Long streetAreaId) { this.streetAreaId = streetAreaId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public POIType getPoiType() { return poiType; }
    public void setPoiType(POIType poiType) { this.poiType = poiType; }
    public Long getStoreId() { return storeId; }
    public void setStoreId(Long storeId) { this.storeId = storeId; }
    public BigDecimal getLongitude() { return longitude; }
    public void setLongitude(BigDecimal longitude) { this.longitude = longitude; }
    public BigDecimal getLatitude() { return latitude; }
    public void setLatitude(BigDecimal latitude) { this.latitude = latitude; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public POIStatus getStatus() { return status; }
    public void setStatus(POIStatus status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
