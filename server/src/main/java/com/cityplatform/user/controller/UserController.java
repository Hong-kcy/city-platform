package com.cityplatform.user.controller;

import com.cityplatform.platform.authentication.AuthenticationInterceptor;
import com.cityplatform.platform.authentication.CurrentUser;
import com.cityplatform.user.application.UserApplicationService;
import com.cityplatform.user.application.dto.UpdateMyProfileRequest;
import com.cityplatform.user.application.readmodel.UserReadModel;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 当前用户 Controller。用户身份一律来自平台登录态（拦截器写入的请求属性），
 * 不提供按前端传入 userId 访问他人资料的入口。
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserApplicationService service;

    public UserController(UserApplicationService service) {
        this.service = service;
    }

    @GetMapping("/me")
    public UserReadModel me(@RequestAttribute(name = AuthenticationInterceptor.CURRENT_USER_ATTRIBUTE,
            required = false) CurrentUser currentUser) {
        return service.me(CurrentUser.required(currentUser).userId());
    }

    @PutMapping("/me")
    public UserReadModel updateMe(@RequestAttribute(name = AuthenticationInterceptor.CURRENT_USER_ATTRIBUTE,
            required = false) CurrentUser currentUser,
                                  @Valid @RequestBody UpdateMyProfileRequest request) {
        return service.updateMe(CurrentUser.required(currentUser).userId(), request);
    }
}
