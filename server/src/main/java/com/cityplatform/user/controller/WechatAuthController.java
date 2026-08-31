package com.cityplatform.user.controller;

import com.cityplatform.user.application.UserApplicationService;
import com.cityplatform.user.application.dto.WechatLoginRequest;
import com.cityplatform.user.application.dto.WechatLoginResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 微信小程序登录 Controller。标准官方链路：
 * wx.login() -> code -> POST /api/auth/wechat/login -> 服务端 code2Session -> openid -> User -> 平台 token。
 */
@RestController
@RequestMapping("/api/auth/wechat")
public class WechatAuthController {

    private final UserApplicationService service;

    public WechatAuthController(UserApplicationService service) {
        this.service = service;
    }

    @PostMapping("/login")
    public WechatLoginResponse login(@Valid @RequestBody WechatLoginRequest request) {
        return service.wechatLogin(request.getCode());
    }
}
