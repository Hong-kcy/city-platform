package com.cityplatform.platform.authentication;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 认证拦截器注册（Web 基础能力）。
 */
@Configuration
public class AuthenticationWebConfig implements WebMvcConfigurer {

    private final AuthenticationInterceptor authenticationInterceptor;

    public AuthenticationWebConfig(AuthenticationInterceptor authenticationInterceptor) {
        this.authenticationInterceptor = authenticationInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor)
                .addPathPatterns("/api/**");
    }
}
