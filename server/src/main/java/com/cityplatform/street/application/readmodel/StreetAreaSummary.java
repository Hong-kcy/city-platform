package com.cityplatform.street.application.readmodel;

/**
 * 街区列表项查询模型。只读，精简字段。
 */
public class StreetAreaSummary {

    private Long id;
    private String name;
    private String coverImageUrl;
    private String status;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCoverImageUrl() { return coverImageUrl; }
    public void setCoverImageUrl(String coverImageUrl) { this.coverImageUrl = coverImageUrl; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
