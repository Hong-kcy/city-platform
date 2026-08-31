package com.cityplatform.merchant.application.readmodel;

import java.time.LocalDateTime;

/**
 * 商户详情查询模型。只读，不进入 Domain。
 * logoUrl 通过 QueryRepository SQL LEFT JOIN stored_file 获取，永不返回 null。
 */
public class MerchantReadModel {

    private Long id;
    private String name;
    private String type;
    private String contactPerson;
    private String contactPhone;
    private String introduction;
    private Long logoFileId;
    private String logoUrl;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getContactPerson() { return contactPerson; }
    public void setContactPerson(String contactPerson) { this.contactPerson = contactPerson; }
    public String getContactPhone() { return contactPhone; }
    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }
    public String getIntroduction() { return introduction; }
    public void setIntroduction(String introduction) { this.introduction = introduction; }
    public Long getLogoFileId() { return logoFileId; }
    public void setLogoFileId(Long logoFileId) { this.logoFileId = logoFileId; }
    public String getLogoUrl() { return logoUrl; }
    public void setLogoUrl(String logoUrl) { this.logoUrl = logoUrl; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
