package com.cityplatform.platform.authentication;

import com.cityplatform.platform.exception.WechatAuthException;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

/**
 * 微信 code2Session 官方接口实现（Infrastructure）。
 * GET https://api.weixin.qq.com/sns/jscode2session
 * 安全约束：
 * 1. AppSecret 仅由服务端配置读取，不下发前端、不硬编码、不写日志；
 * 2. session_key 不返回、不记录日志、不作为平台登录态；
 * 3. HTTP 异常信息可能携带含 secret 的请求 URL，仅记录异常类型，不记录消息体。
 */
@Component
public class HttpWechatClient implements WechatClient {

    private static final Logger log = LoggerFactory.getLogger(HttpWechatClient.class);

    private static final String CODE2SESSION_URL =
            "https://api.weixin.qq.com/sns/jscode2session?appid={appId}&secret={appSecret}"
                    + "&js_code={code}&grant_type=authorization_code";

    private final RestClient restClient;
    private final String appId;
    private final String appSecret;

    public HttpWechatClient(@Value("${wechat.mini-program.app-id:}") String appId,
                            @Value("${wechat.mini-program.app-secret:}") String appSecret) {
        this.appId = appId;
        this.appSecret = appSecret;
        this.restClient = RestClient.create();
    }

    @Override
    public WechatSession code2Session(String code) {
        if (isBlank(appId) || isBlank(appSecret)) {
            throw new WechatAuthException("微信小程序登录配置缺失，请检查服务端环境变量");
        }
        Code2SessionResponse resp;
        try {
            resp = restClient.get()
                    .uri(CODE2SESSION_URL, appId, appSecret, code)
                    .retrieve()
                    .body(Code2SessionResponse.class);
        } catch (RestClientException e) {
            // 异常消息可能包含带 secret 的请求地址，禁止记录原始 message
            log.error("调用微信code2Session失败: {}", e.getClass().getName());
            throw new WechatAuthException("微信登录服务暂不可用，请稍后重试");
        }
        if (resp == null) {
            throw new WechatAuthException("微信登录服务响应为空");
        }
        if (resp.errcode() != null && resp.errcode() != 0) {
            throw new WechatAuthException("微信登录失败(" + resp.errcode() + "): " + resp.errmsg());
        }
        if (isBlank(resp.openid())) {
            throw new WechatAuthException("微信登录响应缺少openid");
        }
        return new WechatSession(resp.openid(), resp.unionid());
    }

    private static boolean isBlank(String s) {
        return s == null || s.isBlank();
    }

    /**
     * 微信官方响应结构。session_key 仅在响应中接收，不对外暴露、不记录日志。
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    private record Code2SessionResponse(Integer errcode,
                                        String errmsg,
                                        String openid,
                                        @JsonProperty("session_key") String sessionKey,
                                        String unionid) {
    }
}
