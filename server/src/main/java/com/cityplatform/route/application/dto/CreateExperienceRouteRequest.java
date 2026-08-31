package com.cityplatform.route.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * 创建路线请求。默认 ACTIVE 状态。
 */
public class CreateExperienceRouteRequest {

    @NotNull(message = "所属街区不能为空")
    private Long streetAreaId;

    @NotBlank(message = "路线名称不能为空")
    @Size(max = 100, message = "路线名称最长100字符")
    private String name;

    @NotBlank(message = "路线主题不能为空")
    @Pattern(regexp = "FRIEND_PHOTO|SOLO_RELAX|FAMILY_FUN|SLOW_WALK|OTHER", message = "路线主题非法")
    private String theme;

    @Size(max = 500, message = "路线描述最长500字符")
    private String description;

    @NotNull(message = "预计时长不能为空")
    private Integer estimatedDuration;

    public Long getStreetAreaId() { return streetAreaId; }
    public void setStreetAreaId(Long streetAreaId) { this.streetAreaId = streetAreaId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getTheme() { return theme; }
    public void setTheme(String theme) { this.theme = theme; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Integer getEstimatedDuration() { return estimatedDuration; }
    public void setEstimatedDuration(Integer estimatedDuration) { this.estimatedDuration = estimatedDuration; }
}
