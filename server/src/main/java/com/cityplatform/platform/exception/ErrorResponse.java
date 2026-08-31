package com.cityplatform.platform.exception;

/**
 * 统一错误响应体。
 */
public record ErrorResponse(String code, String message) {
}
