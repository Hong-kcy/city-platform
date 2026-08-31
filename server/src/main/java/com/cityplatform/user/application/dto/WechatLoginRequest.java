package com.cityplatform.user.application.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * 微信小程序登录请求。code 为 wx.login() 返回的临时登录凭证（一次性）。
 * 前端不携带 appSecret / session_key。
 */
public class WechatLoginRequest {

    @NotBlank(message = "登录凭证code不能为空")
    private String code;

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
}
