package io.project.backend.domain.auth.controller;

import io.project.backend.domain.auth.controller.api.AuthControllerApi;
import io.project.backend.domain.auth.dto.common.AuthTokenDto;
import io.project.backend.domain.auth.dto.common.LoginDto;
import io.project.backend.domain.auth.dto.request.LoginRequest;
import io.project.backend.domain.auth.dto.request.SignupRequest;
import io.project.backend.domain.auth.dto.response.AuthResponse;
import io.project.backend.domain.auth.dto.response.LoginResponse;
import io.project.backend.domain.auth.dto.response.SignupResponse;
import io.project.backend.domain.auth.service.AuthService;
import io.project.backend.global.response.CommonApiResponse;
import io.project.backend.global.security.details.UserDetailsImpl;
import io.project.backend.global.security.jwt.JwtProperties;
import jakarta.validation.Valid;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController implements AuthControllerApi {

  private final JwtProperties jwtProperties;
  private final AuthService authService;

  @PostMapping("/register")
  public ResponseEntity<CommonApiResponse<SignupResponse>> createEmployee(
      @Valid @RequestBody SignupRequest signupRequest
  ) {

    SignupResponse response = authService.createEmployee(signupRequest);

    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(CommonApiResponse.created(response));
  }

  @PostMapping("/login")
  public ResponseEntity<CommonApiResponse<LoginResponse>> login(
      @Valid @RequestBody LoginRequest loginRequest
  ) {

    LoginDto loginDto = authService.login(loginRequest);

    ResponseCookie responseCookie = createRefreshTokenCookie(loginDto.tokens().refreshToken());

    return ResponseEntity
        .status(HttpStatus.OK)
        .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
        .body(CommonApiResponse.ok(
            LoginResponse.from(
                loginDto.tokens().accessToken(),
                loginDto.passwordChangeRequired()
            )
        ));
  }

  @PostMapping("/refresh")
  public ResponseEntity<CommonApiResponse<AuthResponse>> refreshToken(
      @CookieValue(name = "refreshToken", required = false) String refreshToken
  ) {

    AuthTokenDto authTokenDto = authService.refreshToken(refreshToken);

    ResponseCookie responseCookie = createRefreshTokenCookie(authTokenDto.refreshToken());

    return ResponseEntity
        .status(HttpStatus.OK)
        .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
        .body(CommonApiResponse.ok(
            AuthResponse.from(
                authTokenDto.accessToken()
            )
        ));
  }

  @PostMapping("/logout")
  public ResponseEntity<CommonApiResponse<Void>> logout(
      @AuthenticationPrincipal UserDetailsImpl userDetails,
      @CookieValue(name = "refreshToken", required = false) String refreshToken
  ) {

    // 서버에서 토큰 무효화
    authService.logout(userDetails.getUserId(), refreshToken);

    // 클라이언트에서 refresh token 삭제 (만료 시간 0으로 설정)
    ResponseCookie deleteCookie = ResponseCookie.from("refreshToken", "")
        .httpOnly(true)
        .secure(jwtProperties.secure())
        .path("/")
        .sameSite("lax")
        .maxAge(0)
        .build();

    return ResponseEntity
        .status(HttpStatus.OK)
        .header(HttpHeaders.SET_COOKIE, deleteCookie.toString())
        .body(CommonApiResponse.ok(null));
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
