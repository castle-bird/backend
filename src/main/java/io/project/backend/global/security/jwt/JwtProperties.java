package io.project.backend.global.security.jwt;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "app.jwt")
public record JwtProperties(
    @NotBlank
    @Size(min = 32, message = "JWT의 비밀 키는 최소 256비트(32바이트)여야 합니다.")
    String secretKey,

    @Positive(message = "액세스 토큰의 만료 시간은 양수여야 합니다.")
    long accessTokenExpiration,

    @Positive(message = "리프레시 토큰의 만료 시간은 양수여야 합니다.")
    long refreshTokenExpiration,

    boolean secure,

    @NotBlank(message = "JWT 발급자는 필수입니다.")
    String jwtIssuer
) {

}
