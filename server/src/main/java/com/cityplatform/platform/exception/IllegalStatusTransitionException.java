package com.cityplatform.platform.exception;

/**
 * 非法状态迁移异常，对应 HTTP 400。
 */
public class IllegalStatusTransitionException extends BusinessException {

    public IllegalStatusTransitionException(String message) {
        super("ILLEGAL_STATUS", message);
    }
}
