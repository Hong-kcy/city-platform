package com.cityplatform.merchant.domain;

import com.cityplatform.platform.exception.BusinessException;

import java.time.LocalDateTime;

/**
 * 用户优惠券 Entity（独立领取关系）。不挂在 User 或 Coupon 上的从属集合。
 * 一个用户同一张券仅一条记录（数据库 UNIQUE(user_id, coupon_id) 兜底）。
 * redeemCode 领取时生成、全局唯一，是到店核销凭证；二维码由前端根据该值生成，
 * 二维码本身不是领域对象。
 */
public class UserCoupon {

    private Long id;
    private Long couponId;
    private Long userId;
    private UserCouponStatus status;
    private String redeemCode;
    private LocalDateTime redeemedAt;
    private Long redeemedByStoreId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * 工厂方法：用户领取优惠券。
     */
    public static UserCoupon claim(Long couponId, Long userId, String redeemCode) {
        UserCoupon uc = new UserCoupon();
        uc.couponId = couponId;
        uc.userId = userId;
        uc.status = UserCouponStatus.AVAILABLE;
        uc.redeemCode = redeemCode;
        LocalDateTime now = LocalDateTime.now();
        uc.createdAt = now;
        uc.updatedAt = now;
        return uc;
    }

    /**
     * Domain 规则：是否可核销。仅 AVAILABLE 可核销。
     */
    public boolean canRedeem() {
        return status == UserCouponStatus.AVAILABLE;
    }

    /**
     * 核销：AVAILABLE -> REDEEMED，记录核销时间与核销门店。
     */
    public void redeem(Long storeId) {
        if (!canRedeem()) {
            throw new BusinessException("COUPON_ALREADY_REDEEMED", "优惠券已核销");
        }
        this.status = UserCouponStatus.REDEEMED;
        this.redeemedAt = LocalDateTime.now();
        this.redeemedByStoreId = storeId;
        this.updatedAt = this.redeemedAt;
    }

    /**
     * Domain 规则：计算读时有效状态。已核销优先；未核销但券已过期则 EXPIRED。
     * 有效期判断由 Application 传入券的到期时间（跨实体数据不内嵌本 Entity）。
     */
    public String effectiveStatus(LocalDateTime couponValidTo) {
        if (status == UserCouponStatus.REDEEMED) {
            return UserCouponStatus.REDEEMED.name();
        }
        if (couponValidTo != null && LocalDateTime.now().isAfter(couponValidTo)) {
            return "EXPIRED";
        }
        return UserCouponStatus.AVAILABLE.name();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCouponId() { return couponId; }
    public void setCouponId(Long couponId) { this.couponId = couponId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public UserCouponStatus getStatus() { return status; }
    public void setStatus(UserCouponStatus status) { this.status = status; }
    public String getRedeemCode() { return redeemCode; }
    public void setRedeemCode(String redeemCode) { this.redeemCode = redeemCode; }
    public LocalDateTime getRedeemedAt() { return redeemedAt; }
    public void setRedeemedAt(LocalDateTime redeemedAt) { this.redeemedAt = redeemedAt; }
    public Long getRedeemedByStoreId() { return redeemedByStoreId; }
    public void setRedeemedByStoreId(Long redeemedByStoreId) { this.redeemedByStoreId = redeemedByStoreId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
