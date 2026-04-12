package io.project.backend.global.config;

import java.util.HashMap;
import java.util.Map;
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

  // 어노테이션 기반 캐시 설정
  @Bean
  public CacheManager cacheManager(
      RedisConnectionFactory connectionFactory,
      RedisProperties redisProperties
  ) {
    RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
        .entryTtl(redisProperties.getDefaultTtl())
        .disableCachingNullValues() // Null 캐싱처리 안함
        // Key를 문자열로 직렬화 (Redis CLI에서 보기 편하게 하기 위함)
        .serializeKeysWith(
            RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
        // Value는 JSON 형태로 직렬화해서 저장
        .serializeValuesWith(
            RedisSerializationContext.SerializationPair.fromSerializer(
                new GenericJackson2JsonRedisSerializer()));

    Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();

    // RedisProperties에서 정의한 캐시별 TTL 설정을 적용
    if (redisProperties.getCaches() != null) {
      redisProperties.getCaches().forEach((cacheName, cacheSpec) -> {
        cacheConfigurations.put(cacheName, defaultConfig.entryTtl(cacheSpec.ttl()));
      });
    }

    return RedisCacheManager.builder(connectionFactory)
        .cacheDefaults(defaultConfig)
        .withInitialCacheConfigurations(cacheConfigurations)
        .build();
  }

  // RedisTemplate 설정: 직접 RedisTemplate을 사용할 때 직렬화 방식 지정
  @Bean
  public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory connectionFactory) {
    RedisTemplate<String, String> template = new RedisTemplate<>();
    template.setConnectionFactory(connectionFactory);
    // Key를 문자열로 직렬화 (일관된 키 포맷과 Redis CLI 가독성을 위해)
    template.setKeySerializer(new StringRedisSerializer());
    // Value를 문자열로 직렬화 (문자열 기반 데이터 저장/조회 시 직관적으로 확인하기 위해)
    template.setValueSerializer(new StringRedisSerializer());
    return template;
  }
}
