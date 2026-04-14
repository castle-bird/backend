package io.project.backend.domain.auth.controller;

import io.project.backend.domain.auth.dto.common.AuthTokenDto;
import io.project.backend.domain.auth.dto.request.LoginRequest;
import io.project.backend.domain.auth.dto.request.SignupRequest;
import io.project.backend.domain.auth.dto.response.AuthResponse;
import io.project.backend.domain.auth.service.AuthService;
import io.project.backend.global.response.ApiResponse;
import io.project.backend.global.security.jwt.JwtProperties;
import jakarta.validation.Valid;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

  private final JwtProperties jwtProperties;
  private final AuthService authService;

  @PostMapping("/signup")
  public ResponseEntity<ApiResponse<AuthResponse>> signup(
      @Valid @RequestBody SignupRequest signupRequest
  ) {

    AuthTokenDto authTokenDto = authService.signup(signupRequest);

    ResponseCookie responseCookie = createRefreshTokenCookie(authTokenDto.refreshToken());

    return ResponseEntity
        .status(HttpStatus.CREATED)
        .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
        .body(ApiResponse.created(
            AuthResponse.from(
                authTokenDto.accessToken()
            )
        ));
  }

  @PostMapping("/login")
  public ResponseEntity<ApiResponse<AuthResponse>> login(
      @Valid @RequestBody LoginRequest loginRequest
  ) {

    AuthTokenDto authTokenDto = authService.login(loginRequest);

    ResponseCookie responseCookie = createRefreshTokenCookie(authTokenDto.refreshToken());

    return ResponseEntity
        .status(HttpStatus.OK)
        .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
        .body(ApiResponse.ok(
            AuthResponse.from(
                authTokenDto.accessToken()
            )
        ));
  }

  @PostMapping("/refresh")
  public ResponseEntity<ApiResponse<AuthResponse>> refreshToken(
      @CookieValue(name = "refreshToken", required = false) String refreshToken
  ) {

    AuthTokenDto authTokenDto = authService.refreshToken(refreshToken);

    ResponseCookie responseCookie = createRefreshTokenCookie(authTokenDto.refreshToken());

    return ResponseEntity
        .status(HttpStatus.OK)
        .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
        .body(ApiResponse.ok(
            AuthResponse.from(
                authTokenDto.accessToken()
            )
        ));
  }

  /**
   * 지정된 속성으로 secure 및 HTTP-only refresh token cookie를 생성한다.
   *
   * @param refreshToken cookie에 포함할 refresh token
   * @return HTTP\-only, secure flag, SameSite policy, 만료 시간이 설정된 `ResponseCookie`
   */
  private ResponseCookie createRefreshTokenCookie(String refreshToken) {
    return ResponseCookie.from("refreshToken", refreshToken)
        .httpOnly(true)
        .secure(jwtProperties.secure())
        .path("/")
        .sameSite("lax")
        .maxAge(Duration.ofMillis(jwtProperties.refreshTokenExpiration()))
        .build();
  }
}
