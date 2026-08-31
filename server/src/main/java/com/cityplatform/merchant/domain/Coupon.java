package com.cityplatform.merchant.domain;

import com.cityplatform.platform.exception.BusinessException;
import com.cityplatform.platform.exception.IllegalStatusTransitionException;

import java.time.LocalDateTime;

/**
 * 优惠券 Entity（商户商业权益）。纯 POJO，通过 MyBatis XML 映射。
 * 归属 Merchant Domain：主归属门店 storeId，merchantId 由门店派生冗余。
 * 状态仅维护 ACTIVE/INACTIVE；EXPIRED 为读时按有效期计算，不做定时刷状态。
 * 与 Task 无任何绑定，最小可用商业权益模型（无库存/叠加/满减引擎）。
 */
public class Coupon {

    private Long id;
    private Long merchantId;
    private Long storeId;
    private String name;
    private String description;
    private String discountText;
    private LocalDateTime validFrom;
    private LocalDateTime validTo;
    private CouponStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * 工厂方法：商户创建优惠券。新券默认 ACTIVE，创建即对用户可见可领取。
     */
    public static Coupon create(Long merchantId, Long storeId, String name, String description,
                                 String discountText, LocalDateTime validFrom, LocalDateTime validTo) {
        validateTimeRange(validFrom, validTo);
        Coupon c = new Coupon();
        c.merchantId = merchantId;
        c.storeId = storeId;
        c.name = name;
        c.description = description;
        c.discountText = discountText;
        c.validFrom = validFrom;
        c.validTo = validTo;
        c.status = CouponStatus.ACTIVE;
        LocalDateTime now = LocalDateTime.now();
        c.createdAt = now;
        c.updatedAt = now;
        return c;
    }

    /**
     * 修改优惠券信息（PUT 全量替换语义）。已停用优惠券不允许再修改。
     */
    public void updateInfo(String name, String description, String discountText,
                           LocalDateTime validFrom, LocalDateTime validTo) {
        if (this.status == CouponStatus.INACTIVE) {
            throw new IllegalStatusTransitionException("已停用优惠券不允许修改");
        }
        validateTimeRange(validFrom, validTo);
        this.name = name;
        this.description = description;
        this.discountText = discountText;
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        if (this.status == CouponStatus.ACTIVE) {
            throw new IllegalStatusTransitionException("优惠券已是生效状态");
        }
        this.status = CouponStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

    public void deactivate() {
        if (this.status == CouponStatus.INACTIVE) {
            throw new IllegalStatusTransitionException("优惠券已是停用状态");
        }
        this.status = CouponStatus.INACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Domain 规则：是否可被用户领取。需 ACTIVE 且当前时间在有效期内。
     */
    public boolean claimable() {
        return isActiveNow();
    }

    /**
     * Domain 规则：是否可核销。与领取规则一致：ACTIVE 且在有效期内。
     */
    public boolean redeemable() {
        return isActiveNow();
    }

    /**
     * Domain 规则：计算读时有效状态。INACTIVE 优先，其次按有效期判 EXPIRED。
     */
    public String effectiveStatus() {
        if (status == CouponStatus.INACTIVE) {
            return CouponStatus.INACTIVE.name();
        }
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(validFrom)) {
            return "NOT_STARTED";
        }
        if (now.isAfter(validTo)) {
            return "EXPIRED";
        }
        return CouponStatus.ACTIVE.name();
    }

    private boolean isActiveNow() {
        return status == CouponStatus.ACTIVE
                && !LocalDateTime.now().isBefore(validFrom)
                && !LocalDateTime.now().isAfter(validTo);
    }

    private static void validateTimeRange(LocalDateTime validFrom, LocalDateTime validTo) {
        if (validFrom == null || validTo == null || !validTo.isAfter(validFrom)) {
            throw new BusinessException("INVALID_COUPON_TIME", "优惠券有效期结束必须晚于开始");
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getMerchantId() { return merchantId; }
    public void setMerchantId(Long merchantId) { this.merchantId = merchantId; }
    public Long getStoreId() { return storeId; }
    public void setStoreId(Long storeId) { this.storeId = storeId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getDiscountText() { return discountText; }
    public void setDiscountText(String discountText) { this.discountText = discountText; }
    public LocalDateTime getValidFrom() { return validFrom; }
    public void setValidFrom(LocalDateTime validFrom) { this.validFrom = validFrom; }
    public LocalDateTime getValidTo() { return validTo; }
    public void setValidTo(LocalDateTime validTo) { this.validTo = validTo; }
    public CouponStatus getStatus() { return status; }
    public void setStatus(CouponStatus status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
