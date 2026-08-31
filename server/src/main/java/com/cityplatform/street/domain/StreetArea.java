package com.cityplatform.street.domain;

import com.cityplatform.platform.exception.IllegalStatusTransitionException;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 街区 Entity。纯 POJO，无框架注解，通过 MyBatis XML 映射。
 * coverImageFileId 关联 stored_file 表，与 Merchant logoFileId 模式一致。
 */
public class StreetArea {

    private Long id;
    private String name;
    private String introduction;
    private Long coverImageFileId;
    private BigDecimal longitude;
    private BigDecimal latitude;
    private StreetAreaStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * 工厂方法：创建街区。默认 ACTIVE。
     */
    public static StreetArea create(String name, String introduction, Long coverImageFileId,
                                    BigDecimal longitude, BigDecimal latitude) {
        StreetArea sa = new StreetArea();
        sa.name = name;
        sa.introduction = introduction;
        sa.coverImageFileId = coverImageFileId;
        sa.longitude = longitude;
        sa.latitude = latitude;
        sa.status = StreetAreaStatus.ACTIVE;
        LocalDateTime now = LocalDateTime.now();
        sa.createdAt = now;
        sa.updatedAt = now;
        return sa;
    }

    /**
     * 修改基础信息（PUT 全量替换语义）。
     */
    public void updateInfo(String name, String introduction, Long coverImageFileId,
                           BigDecimal longitude, BigDecimal latitude) {
        this.name = name;
        this.introduction = introduction;
        this.coverImageFileId = coverImageFileId;
        this.longitude = longitude;
        this.latitude = latitude;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        if (this.status == StreetAreaStatus.ACTIVE) {
            throw new IllegalStatusTransitionException("街区已是启用状态");
        }
        this.status = StreetAreaStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

    public void deactivate() {
        if (this.status == StreetAreaStatus.INACTIVE) {
            throw new IllegalStatusTransitionException("街区已是停用状态");
        }
        this.status = StreetAreaStatus.INACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getIntroduction() { return introduction; }
    public void setIntroduction(String introduction) { this.introduction = introduction; }
    public Long getCoverImageFileId() { return coverImageFileId; }
    public void setCoverImageFileId(Long coverImageFileId) { this.coverImageFileId = coverImageFileId; }
    public BigDecimal getLongitude() { return longitude; }
    public void setLongitude(BigDecimal longitude) { this.longitude = longitude; }
    public BigDecimal getLatitude() { return latitude; }
    public void setLatitude(BigDecimal latitude) { this.latitude = latitude; }
    public StreetAreaStatus getStatus() { return status; }
    public void setStatus(StreetAreaStatus status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
