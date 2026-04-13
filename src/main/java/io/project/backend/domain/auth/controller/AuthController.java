package io.project.backend.domain.auth.controller;

import io.project.backend.domain.auth.dto.request.SignupRequest;
import io.project.backend.domain.auth.dto.common.AuthTokenDto;
import io.project.backend.domain.auth.dto.response.SignupResponse;
import io.project.backend.domain.auth.service.AuthService;
import io.project.backend.global.response.ApiResponse;
import io.project.backend.global.security.jwt.JwtProperties;
import jakarta.validation.Valid;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

  private final JwtProperties jwtProperties;
  private final AuthService authService;

  @PostMapping("/signup")
  public ResponseEntity<ApiResponse<SignupResponse>> signup(
      @Valid @RequestBody SignupRequest signupRequest
  ) {

    AuthTokenDto authTokenDto = authService.signup(signupRequest);

    ResponseCookie responseCookie = ResponseCookie.from("refreshToken",
            authTokenDto.refreshToken())
        .httpOnly(true)
        .secure(jwtProperties.secure())
        .path("/")
        .sameSite("lax")
        .maxAge(Duration.ofMillis(jwtProperties.refreshTokenExpiration()))
        .build();

    return ResponseEntity
        .status(HttpStatus.CREATED)
        .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
        .body(ApiResponse.created(
            SignupResponse.from(
                authTokenDto.accessToken()
            )
        ));
  }
}
