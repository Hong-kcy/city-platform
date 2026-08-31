package com.cityplatform.user.application.dto;

import com.cityplatform.user.application.readmodel.UserReadModel;

/**
 * 微信登录响应。返回平台登录态 token 与用户信息；
 * 不包含微信 session_key（服务端会话密钥，不下发前端）。
 * 小程序后续请求携带 Authorization: Bearer {token}。
 */
public class WechatLoginResponse {

    private String token;
    private UserReadModel user;

    public WechatLoginResponse() {
    }

    public WechatLoginResponse(String token, UserReadModel user) {
        this.token = token;
        this.user = user;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public UserReadModel getUser() { return user; }
    public void setUser(UserReadModel user) { this.user = user; }
}
