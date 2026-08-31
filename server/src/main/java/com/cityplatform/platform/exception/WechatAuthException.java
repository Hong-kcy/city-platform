package com.cityplatform.platform.exception;

/**
 * 微信认证失败异常（code 无效、code2Session 调用失败、凭证未配置等）。
 * 对应 HTTP 400，由登录接口的业务链路抛出。
 * 注意：异常信息中不得包含 session_key / appSecret 等敏感数据。
 */
public class WechatAuthException extends BusinessException {

    public WechatAuthException(String message) {
        super("WECHAT_AUTH_FAILED", message);
    }
}
