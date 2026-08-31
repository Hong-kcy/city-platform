package com.cityplatform.merchant.controller;

import com.cityplatform.merchant.application.CouponApplicationService;
import com.cityplatform.merchant.application.dto.ChangeCouponStatusRequest;
import com.cityplatform.merchant.application.dto.CouponQuery;
import com.cityplatform.merchant.application.dto.CreateCouponRequest;
import com.cityplatform.merchant.application.dto.RedeemCouponRequest;
import com.cityplatform.merchant.application.dto.UpdateCouponRequest;
import com.cityplatform.merchant.application.readmodel.CouponDetailReadModel;
import com.cityplatform.merchant.application.readmodel.CouponSummary;
import com.cityplatform.merchant.application.readmodel.RedeemResultReadModel;
import com.cityplatform.platform.authentication.AuthenticationInterceptor;
import com.cityplatform.platform.authentication.CurrentUser;
import com.cityplatform.platform.web.PageParam;
import com.cityplatform.platform.web.PageResult;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 优惠券 Controller（管理/查询共用，与活动接口风格一致）。
 * 列表/详情匿名可访问；详情在携带登录态时附带当前用户领取状态。
 * 管理端写操作 TODO: 商户端登录态鉴权（当前沿用项目现有最小边界，与活动管理一致）。
 */
@RestController
@RequestMapping("/api/coupons")
public class CouponController {

    private final CouponApplicationService service;

    public CouponController(CouponApplicationService service) {
        this.service = service;
    }

    @PostMapping
    public CouponSummary create(@Valid @RequestBody CreateCouponRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public CouponSummary update(@PathVariable Long id,
                                @Valid @RequestBody UpdateCouponRequest request) {
        return service.update(id, request);
    }

    /**
     * 优惠券详情。默认为用户公开视图，仅返回启用(ACTIVE)优惠券；
     * 商户管理端携带 management=true 查看全部状态，待鉴权接入后收紧。
     */
    @GetMapping("/{id}")
    public CouponDetailReadModel getDetail(
            @PathVariable Long id,
            @RequestParam(defaultValue = "false") boolean management,
            @RequestAttribute(name = AuthenticationInterceptor.CURRENT_USER_ATTRIBUTE,
                    required = false) CurrentUser currentUser) {
        return service.getDetail(id, currentUser == null ? null : currentUser.userId(), management);
    }

    @GetMapping
    public PageResult<CouponSummary> list(
            @RequestParam(required = false) Long storeId,
            @RequestParam(required = false) Long merchantId,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        CouponQuery query = new CouponQuery();
        query.setStoreId(storeId);
        query.setMerchantId(merchantId);
        query.setStatus(status);
        return service.list(query, new PageParam(page, size));
    }

    @PatchMapping("/{id}/status")
    public CouponSummary changeStatus(@PathVariable Long id,
                                      @Valid @RequestBody ChangeCouponStatusRequest request) {
        return service.changeStatus(id, request);
    }

    /**
     * 商户核销优惠券（Web 后台输入核销码，兼容扫码枪键盘输入）。
     * TODO: 商户端登录态鉴权后，操作门店改由登录态提供。
     */
    @PostMapping("/redeem")
    public RedeemResultReadModel redeem(@Valid @RequestBody RedeemCouponRequest request) {
        return service.redeem(request);
    }
}
