package com.cityplatform.platform.authentication;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HexFormat;
import java.util.List;

/**
 * 数据库持久化登录态实现（Infrastructure）。
 * token 为 32 字节 SecureRandom 十六进制串，明文存储（Demo 阶段无哈希必要），
 * 有效期 30 天；过期 token 由解析时自然失效，不做定时清理。
 */
@Component
public class DatabaseTokenService implements TokenService {

    private static final Duration TOKEN_TTL = Duration.ofDays(30);

    private final JdbcTemplate jdbcTemplate;
    private final SecureRandom random = new SecureRandom();

    public DatabaseTokenService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public String issue(Long userId) {
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        String token = HexFormat.of().formatHex(bytes);
        LocalDateTime now = LocalDateTime.now();
        jdbcTemplate.update(
                "INSERT INTO auth_token (token, user_id, created_at, expires_at) VALUES (?,?,?,?)",
                token, userId, now, now.plus(TOKEN_TTL)
        );
        return token;
    }

    @Override
    public Long resolveUserId(String token) {
        List<Long> userIds = jdbcTemplate.query(
                "SELECT user_id FROM auth_token WHERE token = ? AND expires_at > ?",
                (rs, i) -> rs.getLong("user_id"),
                token, LocalDateTime.now()
        );
        return userIds.isEmpty() ? null : userIds.get(0);
    }
}
