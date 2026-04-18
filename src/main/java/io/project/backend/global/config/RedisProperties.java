package io.project.backend.global.config;

import java.time.Duration;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.redis")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RedisProperties {

  private Duration defaultTtl;
  private Map<String, Duration> caches;
}
