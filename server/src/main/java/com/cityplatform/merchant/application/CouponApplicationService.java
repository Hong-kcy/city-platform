package com.cityplatform.merchant.application;

import com.cityplatform.merchant.application.dto.ChangeCouponStatusRequest;
import com.cityplatform.merchant.application.dto.CouponQuery;
import com.cityplatform.merchant.application.dto.CreateCouponRequest;
import com.cityplatform.merchant.application.dto.RedeemCouponRequest;
import com.cityplatform.merchant.application.dto.UpdateCouponRequest;
import com.cityplatform.merchant.application.readmodel.CouponDetailReadModel;
import com.cityplatform.merchant.application.readmodel.CouponSummary;
import com.cityplatform.merchant.application.readmodel.RedeemResultReadModel;
import com.cityplatform.merchant.application.readmodel.UserCouponDetailReadModel;
import com.cityplatform.merchant.application.readmodel.UserCouponSummary;
import com.cityplatform.merchant.domain.Coupon;
import com.cityplatform.merchant.domain.CouponRepository;
import com.cityplatform.merchant.domain.CouponStatus;
import com.cityplatform.merchant.domain.Store;
import com.cityplatform.merchant.domain.StoreRepository;
import com.cityplatform.merchant.domain.UserCoupon;
import com.cityplatform.merchant.domain.UserCouponRepository;
import com.cityplatform.platform.exception.BusinessException;
import com.cityplatform.platform.exception.IllegalStatusTransitionException;
import com.cityplatform.platform.exception.NotFoundException;
import com.cityplatform.platform.web.PageParam;
import com.cityplatform.platform.web.PageResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 优惠券应用服务。负责优惠券 CRUD、状态流转、用户领取与商户核销的用例编排。
 * 事务边界全部在本层：领取（校验券 + 创建 UserCoupon）、
 * 核销（校验状态 + 条件更新为 REDEEMED）。
 * 核销码生成属于本服务内部能力（SecureRandom），二维码生成留给前端表现层。
 */
@Service
public class CouponApplicationService {

    private static final Logger log = LoggerFactory.getLogger(CouponApplicationService.class);

    /** 核销码字符集：去除易混淆的 0/O/1/I */
    private static final String CODE_ALPHABET = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    private final CouponRepository couponRepository;
    private final CouponQueryRepository couponQueryRepository;
    private final UserCouponRepository userCouponRepository;
    private final UserCouponQueryRepository userCouponQueryRepository;
    private final StoreRepository storeRepository;

    public CouponApplicationService(CouponRepository couponRepository,
                                    CouponQueryRepository couponQueryRepository,
                                    UserCouponRepository userCouponRepository,
                                    UserCouponQueryRepository userCouponQueryRepository,
                                    StoreRepository storeRepository) {
        this.couponRepository = couponRepository;
        this.couponQueryRepository = couponQueryRepository;
        this.userCouponRepository = userCouponRepository;
        this.userCouponQueryRepository = userCouponQueryRepository;
        this.storeRepository = storeRepository;
    }

    // ==================== 商户侧：优惠券管理 ====================

    /**
     * 商户创建优惠券。merchantId 由门店派生，不信任前端传入；
     * TODO: 商户端登录态鉴权后，需校验门店属于当前商户。
     */
    @Transactional
    public CouponSummary create(CreateCouponRequest request) {
        Store store = storeRepository.findById(request.getStoreId());
        if (store == null) {
            throw new NotFoundException("门店不存在: " + request.getStoreId());
        }
        Coupon coupon = Coupon.create(store.getMerchantId(), store.getId(),
                request.getName(), request.getDescription(),
                request.getDiscountText(), request.getValidFrom(), request.getValidTo());
        couponRepository.insert(coupon);
        return couponQueryRepository.findById(coupon.getId());
    }

    @Transactional
    public CouponSummary update(Long id, UpdateCouponRequest request) {
        Coupon coupon = loadCouponOrThrow(id);
        coupon.updateInfo(request.getName(), request.getDescription(),
                request.getDiscountText(), request.getValidFrom(), request.getValidTo());
        couponRepository.update(coupon);
        return couponQueryRepository.findById(id);
    }

    /**
     * 状态变更。合法流转仅 ACTIVE <-> INACTIVE，由 Coupon Entity 维护规则。
     */
    @Transactional
    public CouponSummary changeStatus(Long id, ChangeCouponStatusRequest request) {
        Coupon coupon = loadCouponOrThrow(id);
        CouponStatus target = CouponStatus.valueOf(request.getStatus());
        if (target == CouponStatus.ACTIVE) {
            coupon.activate();
        } else {
            coupon.deactivate();
        }
        couponRepository.update(coupon);
        return couponQueryRepository.findById(id);
    }

    // ==================== 查询 ====================

    public CouponSummary get(Long id) {
        CouponSummary rm = couponQueryRepository.findById(id);
        if (rm == null) {
            throw new NotFoundException("优惠券不存在: " + id);
        }
        fillEffectiveStatus(rm);
        return rm;
    }

    /**
     * 优惠券详情。携带有效登录态时附带当前用户领取状态。
     * 用户公开视图（management=false）仅返回启用(ACTIVE)优惠券，
     * 已停用按不存在处理；管理视图（management=true）可查看全部状态，
     * 待商户端鉴权接入后收紧为登录态校验。
     */
    public CouponDetailReadModel getDetail(Long id, Long userId, boolean management) {
        CouponDetailReadModel rm = couponQueryRepository.findDetailById(id);
        if (rm == null) {
            throw new NotFoundException("优惠券不存在: " + id);
        }
        if (!management && !CouponStatus.ACTIVE.name().equals(rm.getStatus())) {
            throw new NotFoundException("优惠券不存在: " + id);
        }
        fillEffectiveStatus(rm);
        if (userId != null) {
            UserCouponSummary mine = userCouponQueryRepository.findByUserIdAndCouponId(userId, id);
            rm.setClaimed(mine != null);
        }
        return rm;
    }

    public PageResult<CouponSummary> list(CouponQuery query, PageParam page) {
        List<CouponSummary> data = couponQueryRepository.findAll(query, page);
        data.forEach(this::fillEffectiveStatus);
        long total = couponQueryRepository.count(query);
        return new PageResult<>(data, total, page.getPage(), page.getSize());
    }

    // ==================== 用户侧：领取 ====================

    /**
     * 用户领取优惠券。userId 一律来自平台登录态。
     * 规则：仅 ACTIVE 且在有效期内的券可领取；一个用户同一张券只能领取一次，
     * 由 Application 校验 + 数据库 UNIQUE(user_id, coupon_id) 双重保证。
     */
    @Transactional
    public UserCouponDetailReadModel claim(Long couponId, Long userId) {
        Coupon coupon = loadCouponOrThrow(couponId);
        if (!coupon.claimable()) {
            throw new BusinessException("COUPON_NOT_CLAIMABLE",
                    "优惠券当前不可领取（已停用或不在有效期内）");
        }
        UserCoupon existing = userCouponRepository.findByUserIdAndCouponId(userId, couponId);
        if (existing != null) {
            throw new BusinessException("COUPON_ALREADY_CLAIMED", "您已领取过该优惠券");
        }
        UserCoupon userCoupon = UserCoupon.claim(couponId, userId, generateRedeemCode());
        try {
            userCouponRepository.insert(userCoupon);
        } catch (DuplicateKeyException e) {
            // 并发领取兜底：UNIQUE(user_id, coupon_id) 冲突转为业务错误而非 500
            throw new BusinessException("COUPON_ALREADY_CLAIMED", "您已领取过该优惠券");
        }
        log.info("用户领取优惠券: userId={}, couponId={}, redeemCode={}",
                userId, couponId, userCoupon.getRedeemCode());
        return getMyCouponDetail(userId, userCoupon.getId());
    }

    /**
     * 我的优惠券列表（含已核销/已过期，由前端按 effectiveStatus 区分展示）。
     */
    public PageResult<UserCouponSummary> myCoupons(Long userId, PageParam page) {
        List<UserCouponSummary> data = userCouponQueryRepository.findMyCoupons(userId, page);
        data.forEach(uc -> uc.setEffectiveStatus(computeEffectiveStatus(
                uc.getStatus(), uc.getValidTo())));
        long total = userCouponQueryRepository.countMyCoupons(userId);
        return new PageResult<>(data, total, page.getPage(), page.getSize());
    }

    /**
     * 我的优惠券详情（含核销码）。仅本人可查，查询条件含 userId。
     */
    public UserCouponDetailReadModel getMyCouponDetail(Long userId, Long userCouponId) {
        UserCouponDetailReadModel rm = userCouponQueryRepository.findMyCouponDetail(userId, userCouponId);
        if (rm == null) {
            throw new NotFoundException("优惠券领取记录不存在: " + userCouponId);
        }
        rm.setEffectiveStatus(computeEffectiveStatus(rm.getStatus(), rm.getValidTo()));
        return rm;
    }

    // ==================== 商户侧：核销 ====================

    /**
     * 商户核销优惠券（输入核销码，兼容扫码枪键盘输入）。
     * 必须同时满足：UserCoupon=AVAILABLE、Coupon=ACTIVE、当前在有效期、核销码匹配。
     * 重复核销通过"先校验后条件更新"双重防护，条件更新返回 0 行即并发已核销。
     */
    @Transactional
    public RedeemResultReadModel redeem(RedeemCouponRequest request) {
        String redeemCode = request.getRedeemCode() == null ? "" : request.getRedeemCode().trim().toUpperCase();
        UserCoupon userCoupon = userCouponRepository.findByRedeemCode(redeemCode);
        if (userCoupon == null) {
            throw new NotFoundException("核销码不存在: " + redeemCode);
        }
        Coupon coupon = loadCouponOrThrow(userCoupon.getCouponId());

        if (!userCoupon.canRedeem()) {
            throw new BusinessException("COUPON_ALREADY_REDEEMED", "优惠券已核销");
        }
        if (coupon.getStatus() != CouponStatus.ACTIVE) {
            throw new BusinessException("COUPON_NOT_ACTIVE", "优惠券已停用");
        }
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(coupon.getValidFrom())) {
            throw new BusinessException("COUPON_NOT_STARTED", "优惠券未到有效期开始时间");
        }
        if (now.isAfter(coupon.getValidTo())) {
            throw new BusinessException("COUPON_EXPIRED", "优惠券已过期");
        }

        userCoupon.redeem(request.getStoreId());
        int updated = userCouponRepository.markRedeemed(userCoupon);
        if (updated == 0) {
            // 并发核销兜底：条件更新未命中说明已被核销
            throw new BusinessException("COUPON_ALREADY_REDEEMED", "优惠券已核销");
        }
        log.info("优惠券核销成功: redeemCode={}, userCouponId={}, couponId={}",
                redeemCode, userCoupon.getId(), coupon.getId());

        UserCouponDetailReadModel rm = getMyCouponDetail(userCoupon.getUserId(), userCoupon.getId());
        return new RedeemResultReadModel(redeemCode, rm.getId(), rm.getName(),
                rm.getDiscountText(), rm.getStoreName(), rm.getRedeemedAt());
    }

    // ==================== 私有辅助 ====================

    private Coupon loadCouponOrThrow(Long id) {
        Coupon coupon = couponRepository.findById(id);
        if (coupon == null) {
            throw new NotFoundException("优惠券不存在: " + id);
        }
        return coupon;
    }

    /**
     * 生成核销码：CY + 8 位随机（无易混淆字符，SecureRandom）。
     * 全局 UNIQUE(redeem_code) 兜底极小概率冲突。
     */
    private String generateRedeemCode() {
        StringBuilder sb = new StringBuilder("CY");
        for (int i = 0; i < 8; i++) {
            sb.append(CODE_ALPHABET.charAt(RANDOM.nextInt(CODE_ALPHABET.length())));
        }
        return sb.toString();
    }

    private void fillEffectiveStatus(CouponSummary rm) {
        LocalDateTime now = LocalDateTime.now();
        if ("INACTIVE".equals(rm.getStatus())) {
            rm.setEffectiveStatus("INACTIVE");
        } else if (rm.getValidFrom() != null && now.isBefore(rm.getValidFrom())) {
            rm.setEffectiveStatus("NOT_STARTED");
        } else if (rm.getValidTo() != null && now.isAfter(rm.getValidTo())) {
            rm.setEffectiveStatus("EXPIRED");
        } else {
            rm.setEffectiveStatus("ACTIVE");
        }
    }

    private String computeEffectiveStatus(String storedStatus, LocalDateTime validTo) {
        if ("REDEEMED".equals(storedStatus)) {
            return "REDEEMED";
        }
        if (validTo != null && LocalDateTime.now().isAfter(validTo)) {
            return "EXPIRED";
        }
        return storedStatus;
    }
}
