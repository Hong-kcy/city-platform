package com.cityplatform.platform.authentication;

import com.cityplatform.platform.exception.UnauthorizedException;

/**
 * 当前登录用户上下文。由拦截器解析 Authorization: Bearer token 后写入请求属性。
 * userId 一律来自服务端登录态，禁止由前端请求参数指定。
 */
public record CurrentUser(Long userId) {

    /**
     * 校验登录态存在，供需要登录的接口使用；缺失即抛 401。
     */
    public static CurrentUser required(CurrentUser current) {
        if (current == null) {
            throw new UnauthorizedException("未登录或登录已失效");
        }
        return current;
    }
}
