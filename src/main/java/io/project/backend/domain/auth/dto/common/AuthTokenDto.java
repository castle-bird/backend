package io.project.backend.domain.auth.dto.common;

public record AuthTokenDto(
    String accessToken,
    String refreshToken
) {

  public static AuthTokenDto of(String accessToken, String refreshToken) {
    return new AuthTokenDto(accessToken, refreshToken);
  }
}
