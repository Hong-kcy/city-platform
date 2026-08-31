package com.cityplatform.platform.authentication;

/**
 * 平台登录态能力接口（Platform Service）。
 * 微信 openid/session_key 是微信侧身份，不等同于本项目登录态；
 * 登录成功后由本能力签发平台 token，后续接口凭 token 识别 userId。
 * Demo 阶段采用数据库持久化，不引入 Redis。
 */
public interface TokenService {

    /**
     * 为用户签发新的平台登录令牌。
     */
    String issue(Long userId);

    /**
     * 根据 token 解析用户 ID。
     *
     * @return 有效 token 返回 userId；token 无效或已过期返回 null
     */
    Long resolveUserId(String token);
}
