package com.sprint.mission.discodeit.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching // Spring Cache 활성화
public class CacheConfig {

    @Bean
    public CacheManager caffeineCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager(
                "channelCache", "channelCacheByUserId",
                "notificationCache",
                "userCache", "userCacheAll");
        cacheManager.setCaffeine(
                Caffeine.newBuilder()
                        .maximumSize(100) // 캐시 크기 제한
                        .expireAfterAccess(600, TimeUnit.SECONDS)
                        .recordStats() // 통계 수집 활성화
                // TODO: 캐시 항목 제거 리스너 필요?
        );

        return cacheManager;
    }
}
