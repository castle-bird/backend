package io.project.backend.domain.auth.dto.response;

public record SignupResponse(
    String email,
    String password
) {

  public static SignupResponse form(String email, String password) {
    return new SignupResponse(email, password);
  }
}
