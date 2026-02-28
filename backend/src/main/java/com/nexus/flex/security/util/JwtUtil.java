package com.nexus.flex.security.util;

import com.nexus.flex.security.CustomUserDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;

/**
 * @author : 11's papa
 * @since : 2026/2/25, 星期三
 **/
@Component
public class JwtUtil {
    @Value("${jwt.secret:mySuperSecretKeyForSpringBoot4ProjectThatIsLongEnough}")
    private String secret;
    @Value("${jwt.access-expiration:3600000}") // 1小时
    private long accessExpiration;
    @Value("${jwt.refresh-expiration:604800000}") // 7天
    private long refreshExpiration;

    private final RedissonClient redissonClient;
    private static final String USER_DETAILS_KEY_PREFIX = "auth:refresh_token:";

    public JwtUtil(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateAccessToken(CustomUserDetails userDetail) {
        return generateToken(userDetail, "access", accessExpiration);
    }

    public String generateRefreshToken(CustomUserDetails userDetail) {
        String token = generateToken(userDetail, "refresh", refreshExpiration);
        redissonClient.getBucket(USER_DETAILS_KEY_PREFIX + userDetail.getUsername()).set(token, Duration.ofSeconds(refreshExpiration));
        return token;
    }

    public Claims parseClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            // 即使过期也要返回claims以获取用户信息用于刷新
            // return e.getClaims();
            throw new JwtException("Invalid or expired token");
        }
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token);
            // 这里可以扩展：检查 Redis 中是否存在该 token 的黑名单标记（如果需要即时撤销 access_token）
            // 但通常 access_token 短效，不存 Redis，仅靠过期时间；refresh_token 存 Redis 控制
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public void invalidateRefreshToken(String username) {
        redissonClient.getBucket(USER_DETAILS_KEY_PREFIX + username).delete();
    }

    public boolean isRefreshTokenValid(String username, String token) {
        RBucket<String> bucket = redissonClient.getBucket(USER_DETAILS_KEY_PREFIX + username);
        String storedToken = bucket.get();
        return token != null && token.equals(storedToken);
    }

    private String generateToken(CustomUserDetails userDetail, String type, long expiration) {
        JwtBuilder builder = Jwts.builder()
                .subject(userDetail.getUsername())
                .claim("userId", userDetail.getUserId())
                .claim("type", type)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey());
        if ("access".equals(type)) {
            builder.claim("roles", userDetail.getAuthorities());
        }
        return builder.compact();
    }
}
