package com.cityplatform.platform.exception;

/**
 * 未登录或登录态失效异常，对应 HTTP 401。
 * 由需要登录态的接口在缺少当前用户时抛出。
 */
public class UnauthorizedException extends BusinessException {

    public UnauthorizedException(String message) {
        super("UNAUTHORIZED", message);
    }
}
