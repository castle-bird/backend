package io.project.backend.global.security.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.jwt")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class JwtProperties {

  private String secretKey;
  private long AccessTokenExpiration;
  private long RefreshTokenExpiration;
  private boolean secure;
}
