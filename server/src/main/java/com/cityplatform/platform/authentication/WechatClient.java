package com.cityplatform.platform.authentication;

/**
 * 微信 code2Session 能力接口（Platform Service）。
 * 微信 SDK / HTTP 调用属于 Infrastructure，由本接口的实现承担；
 * 业务域 Application 层只依赖本接口，不感知微信 SDK。
 */
public interface WechatClient {

    /**
     * 调用微信官方 code2Session 接口，用临时登录凭证 code 换取用户身份。
     * code 只能使用一次；session_key 为服务端会话密钥，不向调用方暴露。
     *
     * @param code wx.login() 获得的临时登录凭证
     * @return 微信会话身份（openid / unionid）
     */
    WechatSession code2Session(String code);

    /**
     * 微信会话身份。仅承载用户身份字段，
     * 不包含 session_key（当前阶段无微信加密数据解密用例）。
     */
    record WechatSession(String openid, String unionid) {
    }
}
