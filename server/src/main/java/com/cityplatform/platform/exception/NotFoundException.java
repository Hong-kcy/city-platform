package com.cityplatform.platform.exception;

/**
 * 资源不存在异常，对应 HTTP 404。
 */
public class NotFoundException extends BusinessException {

    public NotFoundException(String message) {
        super("NOT_FOUND", message);
    }
}
