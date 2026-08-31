package com.cityplatform.merchant.domain;

import com.cityplatform.platform.exception.IllegalStatusTransitionException;

import java.time.LocalDateTime;

/**
 * 商户 Entity。纯 POJO，无框架注解，通过 MyBatis XML 映射。
 * 业务规则封装在行为方法中。
 */
public class Merchant {

    private Long id;
    private String name;
    private MerchantType type;
    private String contactPerson;
    private String contactPhone;
    private String introduction;
    private Long logoFileId;
    private MerchantStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * 工厂方法：创建商户。新商户默认 ACTIVE。
     */
    public static Merchant create(String name, MerchantType type, String contactPerson,
                                  String contactPhone, String introduction, Long logoFileId) {
        Merchant m = new Merchant();
        m.name = name;
        m.type = type;
        m.contactPerson = contactPerson;
        m.contactPhone = contactPhone;
        m.introduction = introduction;
        m.logoFileId = logoFileId;
        m.status = MerchantStatus.ACTIVE;
        LocalDateTime now = LocalDateTime.now();
        m.createdAt = now;
        m.updatedAt = now;
        return m;
    }

    /**
     * 修改基础信息（PUT 全量替换语义）。
     */
    public void updateInfo(String name, MerchantType type, String contactPerson,
                           String contactPhone, String introduction, Long logoFileId) {
        this.name = name;
        this.type = type;
        this.contactPerson = contactPerson;
        this.contactPhone = contactPhone;
        this.introduction = introduction;
        this.logoFileId = logoFileId;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        if (this.status == MerchantStatus.ACTIVE) {
            throw new IllegalStatusTransitionException("商户已是启用状态");
        }
        this.status = MerchantStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

    public void deactivate() {
        if (this.status == MerchantStatus.INACTIVE) {
            throw new IllegalStatusTransitionException("商户已是停用状态");
        }
        this.status = MerchantStatus.INACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public MerchantType getType() { return type; }
    public void setType(MerchantType type) { this.type = type; }
    public String getContactPerson() { return contactPerson; }
    public void setContactPerson(String contactPerson) { this.contactPerson = contactPerson; }
    public String getContactPhone() { return contactPhone; }
    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }
    public String getIntroduction() { return introduction; }
    public void setIntroduction(String introduction) { this.introduction = introduction; }
    public Long getLogoFileId() { return logoFileId; }
    public void setLogoFileId(Long logoFileId) { this.logoFileId = logoFileId; }
    public MerchantStatus getStatus() { return status; }
    public void setStatus(MerchantStatus status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
