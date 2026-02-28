package com.nexus.flex.security.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.mybatisflex.core.query.QueryChain;
import com.nexus.flex.modules.system.entity.SystemUser;
import com.nexus.flex.modules.system.entity.UserRolePermission;
import com.nexus.flex.modules.system.service.UserRoleRelationService;
import com.nexus.flex.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

/**
 * @author : 11's papa
 * @since : 2026/2/27, 星期五
 **/
@Service
@RequiredArgsConstructor
public class UserCacheService {
    // 手动创建 Caffeine 缓存实例，以便更精细控制
    private final Cache<String, CustomUserDetails> localCache;
    private final RedissonClient redissonClient;
    // private final RedisTemplate<String, Object> redisTemplate;
    private final UserRoleRelationService userRoleRelationService;
    // Redis Key 前缀
    private static final String USER_DETAILS_KEY_PREFIX = "auth:user_details:";
    private static final long LOCAL_EXPIRE_MINUTES = 10;
    private static final long REDIS_EXPIRE_MINUTES = 60; // Redis 存活时间应长于本地

    public CustomUserDetails loadUserByUsername(String username) {
        // --- Level 1: Caffeine ---
        CustomUserDetails userDetails = localCache.getIfPresent(username);
        if (userDetails != null) {
            return userDetails;
        }
        // --- Level 2: Redisson ---
        String redisKey = USER_DETAILS_KEY_PREFIX + username;
        // Object userDetailsFromRedis = redisTemplate.opsForValue().get(redisKey);
        RBucket<CustomUserDetails> bucket = redissonClient.getBucket(redisKey);
        userDetails = bucket.get();
        // userDetails = JSON.objectToType(CustomUserDetails.class).apply(userDetailsFromRedis);

        if (userDetails != null) {
            // 命中 Redis，回写本地缓存 (预热)
            localCache.put(username, userDetails);
            return userDetails;
        }
        // --- Level 3: Database ---
        SystemUser systemUser = QueryChain.of(SystemUser.class).where(SystemUser::getName).eq(username)
                .oneOpt()
                .orElseThrow(() -> new UsernameNotFoundException("用户不存在"));
        List<String> roles = userRoleRelationService.getRoleIdsByUserId(systemUser.getId());
        List<String> permissions = QueryChain.of(UserRolePermission.class).where(UserRolePermission::getRoleId).in(roles).list().stream()
                .map(UserRolePermission::getPermissionId).toList();
        userDetails = new CustomUserDetails(systemUser, roles, permissions);
        // --- 回写缓存 ---
        // 1. 写 Redis
        bucket.set(userDetails, Duration.ofMinutes(REDIS_EXPIRE_MINUTES)); // 实际应设具体值
        bucket.expire(Duration.ofMinutes(REDIS_EXPIRE_MINUTES));
        // redisTemplate.opsForValue().set(redisKey, userDetails, Duration.ofMinutes(REDIS_EXPIRE_MINUTES));
        // 2. 写 Caffeine
        localCache.put(username, userDetails);
        return userDetails;
    }

    public void evictUserCache(String username) {
        // 1. 删本地
        localCache.invalidate(username);

        // 2. 删 Redis
        String redisKey = USER_DETAILS_KEY_PREFIX + username;
        redissonClient.getBucket(redisKey).delete();
        // redisTemplate.delete(redisKey);
    }

    public void updateUserCache(CustomUserDetails userDetails) {
        String username = userDetails.getUsername();
        // 更新本地
        localCache.put(username, userDetails);
        // 更新 Redis
        String redisKey = USER_DETAILS_KEY_PREFIX + username;
        redissonClient.getBucket(redisKey).set(userDetails, Duration.ofMinutes(REDIS_EXPIRE_MINUTES));
        // redisTemplate.opsForValue().set(redisKey, userDetails, Duration.ofMinutes(REDIS_EXPIRE_MINUTES));
    }
}
