package com.cityplatform.platform.authentication;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 登录态解析拦截器。
 * 仅当请求携带 Authorization: Bearer <token> 时解析并写入当前用户属性，
 * 本身不做强制拦截（放行所有请求）；是否必须登录由各接口自行声明，
 * 从而支持活动列表/详情等匿名可访问、订阅等必须登录的混合场景。
 */
@Component
public class AuthenticationInterceptor implements HandlerInterceptor {

    public static final String CURRENT_USER_ATTRIBUTE = "currentUser";
    private static final String BEARER_PREFIX = "Bearer ";

    private final TokenService tokenService;

    public AuthenticationInterceptor(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith(BEARER_PREFIX)) {
            String token = header.substring(BEARER_PREFIX.length()).trim();
            if (!token.isEmpty()) {
                Long userId = tokenService.resolveUserId(token);
                if (userId != null) {
                    request.setAttribute(CURRENT_USER_ATTRIBUTE, new CurrentUser(userId));
                }
            }
        }
        return true;
    }
}
