package com.projectsync.iamuser.config;

import com.projectsync.iamuser.dto.response.UserResponse;
import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.jsr107.Eh107Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.spi.CachingProvider;
import java.time.Duration;

@Configuration
public class AppConfig {

    @Bean
    public CacheManager EhcacheManager() {

        CacheConfiguration<String, UserResponse> cacheConfig = CacheConfigurationBuilder
                .newCacheConfigurationBuilder(String.class,
                        UserResponse.class,
                        ResourcePoolsBuilder.newResourcePoolsBuilder().offheap(10, MemoryUnit.MB).build())
                .withExpiry(ExpiryPolicyBuilder.timeToIdleExpiration(Duration.ofSeconds(25)))
                .build();

        CachingProvider cachingProvider = Caching.getCachingProvider();
        CacheManager cacheManager = cachingProvider.getCacheManager();

        javax.cache.configuration.Configuration<String, UserResponse> configuration = Eh107Configuration
                .fromEhcacheCacheConfiguration(cacheConfig);
        cacheManager.createCache("cacheStore", configuration);

        return cacheManager;
    }

    @Bean
    public PaginationProperties getPaginationProperties() {

        return new PaginationProperties();
    }
}