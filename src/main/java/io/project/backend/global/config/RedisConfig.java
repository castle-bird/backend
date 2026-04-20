package io.project.backend.global.config;

import java.time.Duration;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableCaching
@EnableConfigurationProperties(RedisProperties.class)
public class RedisConfig {

  @Bean
  public CacheManager cacheManager(
      RedisConnectionFactory connectionFactory,
      RedisProperties redisProperties
  ) {

    // 기본 캐시 설정
    RedisCacheConfiguration cacheDefaults = RedisCacheConfiguration.defaultCacheConfig()
        .entryTtl(
            redisProperties.getDefaultTtl() == null
                ? Duration.ofMinutes(10)
                : redisProperties.getDefaultTtl()
        )
        .disableCachingNullValues()
        .serializeValuesWith(
            RedisSerializationContext.SerializationPair.fromSerializer(
                new GenericJackson2JsonRedisSerializer())
        );

    // 개별 캐시 설정
    Map<String, Duration> caches = redisProperties.getCaches() == null
        ? Map.of()
        : redisProperties.getCaches();

    Map<String, RedisCacheConfiguration> cacheConfigurations = caches.entrySet().stream()
        .collect(Collectors.toMap(
            Entry::getKey,
            entry -> cacheDefaults.entryTtl(entry.getValue())
        ));

    return RedisCacheManager.builder(connectionFactory)
        .cacheDefaults(cacheDefaults)
        .withInitialCacheConfigurations(cacheConfigurations)
        .build();
  }

  @Bean
  public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory connectionFactory) {
    RedisTemplate<String, String> template = new RedisTemplate<>();
    template.setConnectionFactory(connectionFactory);
    template.setKeySerializer(new StringRedisSerializer());
    template.setValueSerializer(new StringRedisSerializer());
    return template;
  }
}
