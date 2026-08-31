package com.cityplatform.merchant.controller;

import com.cityplatform.merchant.application.CouponApplicationService;
import com.cityplatform.merchant.application.readmodel.UserCouponDetailReadModel;
import com.cityplatform.merchant.application.readmodel.UserCouponSummary;
import com.cityplatform.platform.authentication.AuthenticationInterceptor;
import com.cityplatform.platform.authentication.CurrentUser;
import com.cityplatform.platform.web.PageParam;
import com.cityplatform.platform.web.PageResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户优惠券 Controller（领取/我的优惠券）。
 * userId 一律来自平台登录态（拦截器写入的请求属性），不接受前端传入 userId。
 */
@RestController
public class UserCouponController {

    private final CouponApplicationService service;

    public UserCouponController(CouponApplicationService service) {
        this.service = service;
    }

    @PostMapping("/api/coupons/{couponId}/claim")
    public UserCouponDetailReadModel claim(
            @PathVariable Long couponId,
            @RequestAttribute(name = AuthenticationInterceptor.CURRENT_USER_ATTRIBUTE,
                    required = false) CurrentUser currentUser) {
        return service.claim(couponId, CurrentUser.required(currentUser).userId());
    }

    @GetMapping("/api/users/me/coupons")
    public PageResult<UserCouponSummary> myCoupons(
            @RequestAttribute(name = AuthenticationInterceptor.CURRENT_USER_ATTRIBUTE,
                    required = false) CurrentUser currentUser,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        return service.myCoupons(CurrentUser.required(currentUser).userId(),
                new PageParam(page, size));
    }

    @GetMapping("/api/users/me/coupons/{id}")
    public UserCouponDetailReadModel myCouponDetail(
            @PathVariable Long id,
            @RequestAttribute(name = AuthenticationInterceptor.CURRENT_USER_ATTRIBUTE,
                    required = false) CurrentUser currentUser) {
        return service.getMyCouponDetail(CurrentUser.required(currentUser).userId(), id);
    }
}
