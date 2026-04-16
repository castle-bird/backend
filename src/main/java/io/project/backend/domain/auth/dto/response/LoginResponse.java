package io.project.backend.domain.auth.dto.response;

public record LoginResponse(
    String accessToken,
    boolean passwordChangeRequired
) {

  public static LoginResponse from(String accessToken, boolean passwordChangeRequired) {
    return new LoginResponse(accessToken, passwordChangeRequired);
  }
}
