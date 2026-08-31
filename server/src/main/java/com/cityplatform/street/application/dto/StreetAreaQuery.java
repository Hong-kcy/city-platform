package com.cityplatform.street.application.dto;

/**
 * 街区列表查询条件。
 */
public class StreetAreaQuery {

    private String name;
    private String status;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
