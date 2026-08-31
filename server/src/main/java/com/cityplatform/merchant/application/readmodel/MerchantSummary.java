package com.cityplatform.merchant.application.readmodel;

/**
 * 商户列表项查询模型。只读，精简字段。
 */
public class MerchantSummary {

    private Long id;
    private String name;
    private String type;
    private String status;
    private String logoUrl;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getLogoUrl() { return logoUrl; }
    public void setLogoUrl(String logoUrl) { this.logoUrl = logoUrl; }
}
