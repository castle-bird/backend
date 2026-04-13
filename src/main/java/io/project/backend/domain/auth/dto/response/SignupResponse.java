package io.project.backend.domain.auth.dto.response;

public record SignupResponse(
    String accessToken
) {
  public static SignupResponse from(String accessToken) {
    return new SignupResponse(accessToken);
  }
}
