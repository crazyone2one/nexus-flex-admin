package com.nexus.flex.security.filter;

import com.nexus.flex.security.handler.OptimizedAuthenticationEntryPoint;
import com.nexus.flex.security.service.UserCacheService;
import com.nexus.flex.security.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.Objects;

/**
 * @author : 11's papa
 * @since : 2026/2/25, 星期三
 **/
@Slf4j
@NullMarked
@Component
@RequiredArgsConstructor
public class OptimizedAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final HandlerExceptionResolver handlerExceptionResolver;
    private final OptimizedAuthenticationEntryPoint authenticationEntryPoint;
    private final UserCacheService userCacheService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            try {
                Claims claims = jwtUtil.parseClaims(token);
                if (jwtUtil.validateToken(token)) {
                    String username = claims.getSubject();
                    if (Objects.nonNull(username) && SecurityContextHolder.getContext().getAuthentication() == null) {
                        UserDetails userDetails = userCacheService.loadUserByUsername(username);
                        // 构建 Authentication 对象
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            } catch (io.jsonwebtoken.JwtException exception) {
                // JWT 相关异常应该导致认证失败，而不是服务器错误
                AuthenticationException authException = new BadCredentialsException("Token is invalid", exception);
                SecurityContextHolder.clearContext();
                authenticationEntryPoint.commence(request, response, authException);
            } catch (Exception exception) {
                // Token 无效或过期，不设置 SecurityContext，让后续逻辑处理
                // 可以在 request attribute 中标记过期原因，供全局异常或控制器读取
                log.error("JWT 验证失败：{}", exception.getMessage(), exception);
                handlerExceptionResolver.resolveException(request, response, null, exception);
            }
        }
        filterChain.doFilter(request, response);
    }
}
