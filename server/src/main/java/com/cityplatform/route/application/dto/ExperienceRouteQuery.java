package com.cityplatform.route.application.dto;

/**
 * 路线查询条件。
 */
public class ExperienceRouteQuery {

    private Long streetAreaId;
    private String name;
    private String theme;
    private String status;

    public Long getStreetAreaId() { return streetAreaId; }
    public void setStreetAreaId(Long streetAreaId) { this.streetAreaId = streetAreaId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getTheme() { return theme; }
    public void setTheme(String theme) { this.theme = theme; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
