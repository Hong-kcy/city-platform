package com.cityplatform.platform.exception;

/**
 * 业务异常基类。规范：统一业务异常体系，禁止直接抛出数据库/网络/SDK异常。
 */
public class BusinessException extends RuntimeException {

    private final String code;

    public BusinessException(String code, String message) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
