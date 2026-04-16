package io.project.backend.domain.auth.dto.common;

public record LoginDto(
    AuthTokenDto tokens,
    boolean passwordChangeRequired
) {

  public static LoginDto from(
      AuthTokenDto tokens,
      boolean passwordChangeRequired
  ) {
    return new LoginDto(tokens, passwordChangeRequired);
  }
}
