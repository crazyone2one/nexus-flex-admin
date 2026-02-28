package com.nexus.flex.modules.system.controller;

import com.nexus.flex.common.request.LoginReq;
import com.nexus.flex.common.result.HttpResultCode;
import com.nexus.flex.common.result.ResultHolder;
import com.nexus.flex.security.CustomUserDetails;
import com.nexus.flex.security.service.UserCacheService;
import com.nexus.flex.security.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : 11's papa
 * @since : 2026/2/25, 星期三
 **/
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserCacheService userCacheService;

    /**
     * 1. 用户登录
     * 流程：
     * 1. 构建未认证的 Token (UsernamePasswordAuthenticationToken)
     * 2. 调用 AuthenticationManager.authenticate 进行验证 (内部调用 UserDetailsService)
     * 3. 验证通过后，从 Authentication 中获取 OptimizedUserDetail
     * 4. 生成 access_token 和 refresh_token
     * 5. 将 refresh_token 存入 Redis
     */
    @PostMapping("/login")
    public Map<String, String> login(@RequestBody LoginReq loginReq) {

        // 1. 创建未认证的 Authentication 对象.注意：此时 credentials 是明文密码
        UsernamePasswordAuthenticationToken authRequest =
                new UsernamePasswordAuthenticationToken(loginReq.username(), loginReq.password());
        // 2. 执行认证 (如果失败会抛出 BadCredentialsException 等)
        // 成功后，authResult.getPrincipal() 就是 loadUserByUsername 返回的 OptimizedUserDetail
        Authentication authResult = authenticationManager.authenticate(authRequest);
        // SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        // securityContext.setAuthentication(authResult);
        Object principal = authResult.getPrincipal();
        if (principal instanceof CustomUserDetails customUserDetails) {
            // 2. 生成 Token
            String accessToken = jwtUtil.generateAccessToken(customUserDetails);
            String refreshToken = jwtUtil.generateRefreshToken(customUserDetails);

            Map<String, String> tokens = new HashMap<>();
            tokens.put("access_token", accessToken);
            tokens.put("refresh_token", refreshToken);

            return tokens;
        }
        return Map.of();
    }

    /**
     * 2. 刷新 Token
     * 流程：
     * 1. 从 Header 获取 Refresh Token
     * 2. 解析 Token 获取用户名
     * 3. 【关键】校验 Redis 中是否存在该 Refresh Token (防止已退出)
     * 4. 校验 JWT 签名和过期时间
     * 5. 生成新的 Access Token (可选：生成新的 Refresh Token 并覆盖 Redis)
     */
    @PostMapping("/refresh")
    public ResultHolder refresh(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResultHolder.error(HttpResultCode.UNAUTHORIZED.getCode(), "缺少 Refresh Token");
        }
        String refreshToken = authHeader.substring(7);

        try {
            var claims = jwtUtil.parseClaims(refreshToken);
            String username = claims.getSubject();

            // 检查是否是 refresh token 类型
            if (!"refresh".equals(claims.get("type"))) {
                return ResultHolder.error(HttpResultCode.UNAUTHORIZED.getCode(), "Token 类型错误，请使用 Refresh Token");
            }

            // 检查 Redis 中该用户的 refresh token 是否有效 (防止已退出)
            if (!jwtUtil.isRefreshTokenValid(username, refreshToken)) {
                return ResultHolder.error(HttpResultCode.UNAUTHORIZED.getCode(), "Refresh Token 已失效，请重新登录");
            }
            CustomUserDetails userDetail;
            try {
                userDetail = userCacheService.loadUserByUsername(username);
            } catch (Exception e) {
                return ResultHolder.error(HttpResultCode.UNAUTHORIZED.getCode(), "用户不存在或状态异常");
            }
            // 生成新的 Access Token (可选：同时也轮换 Refresh Token，这里仅刷新 Access)
            String newAccessToken = jwtUtil.generateAccessToken(userDetail);

            // 如果需要轮换 Refresh Token，在这里生成新的并覆盖 Redis
            // String newRefreshToken = jwtUtil.generateRefreshToken(username, userId);

            Map<String, String> result = new HashMap<>();
            result.put("access_token", newAccessToken);
            // result.put("refresh_token", newRefreshToken);

            return ResultHolder.success(result);
        } catch (Exception e) {
            return ResultHolder.error(401, "Refresh Token 过期或无效，请重新登录");
        }
    }

    @PostMapping("/logout")
    public ResultHolder logout(@RequestHeader("Authorization") String authHeader) {
        // 这里通常传的是 Access Token，但我们要失效的是 Refresh Token
        // 前端应该在 logout 时同时发送 refresh_token，或者我们在 Access Token 里也能解析出 username
        // 假设前端在 Header 中传了 Access Token，我们解析出 username 去删 Redis 里的 Refresh Token

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String accessToken = authHeader.substring(7);
            try {
                var claims = jwtUtil.parseClaims(accessToken);
                String username = claims.getSubject();
                jwtUtil.invalidateRefreshToken(username);
                // 清除用户详情二级缓存
                userCacheService.evictUserCache(username);
                return ResultHolder.success(null);
            } catch (Exception e) {
                // 即使 token 解析失败，也尝试清理（如果有其他方式获取 username）
                return ResultHolder.success(null);
            }
        }
        return ResultHolder.success(null);
    }
}
