package com.cityplatform.recommendation.application.readmodel;

/**
 * 推荐卡片读模型（最终对外输出，不嵌套任何业务域 Entity）。
 * 前端点击后按 targetType/targetId 跳转对应详情页；
 * 领取优惠券等动作仍由对应业务域接口完成。
 */
public class RecommendationCardReadModel {

    private int id;
    /** 推荐对象类型：STORE / ACTIVITY / EXPERIENCE_ROUTE */
    private String type;
    private Long targetId;
    private String title;
    private String subtitle;
    /** 规则生成的可解释推荐理由 */
    private String reason;
    private String coverImageUrl;
    /** 仅 STORE 候选有值：门店当前是否有可领取优惠券 */
    private Boolean hasCoupon;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public Long getTargetId() { return targetId; }
    public void setTargetId(Long targetId) { this.targetId = targetId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getSubtitle() { return subtitle; }
    public void setSubtitle(String subtitle) { this.subtitle = subtitle; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public String getCoverImageUrl() { return coverImageUrl; }
    public void setCoverImageUrl(String coverImageUrl) { this.coverImageUrl = coverImageUrl; }
    public Boolean getHasCoupon() { return hasCoupon; }
    public void setHasCoupon(Boolean hasCoupon) { this.hasCoupon = hasCoupon; }
}
