package com.nexus.flex.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.nexus.flex.security.CustomUserDetails;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * @author : 11's papa
 * @since : 2026/2/27, 星期五
 **/
@Configuration
public class CacheConfig {
    /**
     * 配置 Caffeine 本地缓存管理器
     * 主要用于存储热点用户详情
     */
    @Bean
    public CacheManager caffeineCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("userDetailsCache");
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .maximumSize(1000)          // 最多存 1000 个用户对象
                .initialCapacity(100)       // 初始大小
                .expireAfterWrite(10, TimeUnit.MINUTES) // 写入后 10 分钟过期
                .recordStats());            // 开启统计监控
        return cacheManager;
    }

    @Bean
    public Cache<String, CustomUserDetails> userDetailsCache() {
        return Caffeine.newBuilder()
                .maximumSize(1000)
                .initialCapacity(100)
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .recordStats()
                .build();
    }
}
