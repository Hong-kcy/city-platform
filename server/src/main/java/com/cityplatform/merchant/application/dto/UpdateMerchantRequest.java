package com.cityplatform.merchant.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * 修改商户请求（PUT 全量替换语义，核心字段必填）。
 */
public class UpdateMerchantRequest {

    @NotBlank(message = "商户名称不能为空")
    @Size(max = 100, message = "商户名称最长100字符")
    private String name;

    @NotBlank(message = "商户类型不能为空")
    @Pattern(regexp = "FOOD|RETAIL|ENTERTAINMENT|SERVICE|OTHER", message = "商户类型非法")
    private String type;

    @Size(max = 50, message = "联系人最长50字符")
    private String contactPerson;

    @Size(max = 20, message = "联系电话最长20字符")
    private String contactPhone;

    @Size(max = 500, message = "简介最长500字符")
    private String introduction;

    private Long logoFileId;

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
}
