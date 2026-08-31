package com.cityplatform.recommendation.application.readmodel;

/**
 * 体验路线候选读模型（跨域聚合 SQL 投影，仅供推荐候选准备使用）。
 */
public class RouteCandidateReadModel {

    private Long id;
    private String name;
    private String theme;
    private Integer estimatedDuration;
    private String coverImageUrl;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getTheme() { return theme; }
    public void setTheme(String theme) { this.theme = theme; }
    public Integer getEstimatedDuration() { return estimatedDuration; }
    public void setEstimatedDuration(Integer estimatedDuration) { this.estimatedDuration = estimatedDuration; }
    public String getCoverImageUrl() { return coverImageUrl; }
    public void setCoverImageUrl(String coverImageUrl) { this.coverImageUrl = coverImageUrl; }
}
