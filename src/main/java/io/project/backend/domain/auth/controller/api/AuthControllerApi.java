package io.project.backend.domain.auth.controller.api;


import io.project.backend.domain.auth.dto.request.LoginRequest;
import io.project.backend.domain.auth.dto.request.SignupRequest;
import io.project.backend.domain.auth.dto.response.AuthResponse;
import io.project.backend.global.response.CommonApiResponse;
import io.project.backend.global.security.details.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Auth", description = "인증 및 토큰 관련 API")
public interface AuthControllerApi {


  @Operation(summary = "회원가입", description = "사용자를 등록하고 access token을 발급합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "회원가입 성공"),
      @ApiResponse(responseCode = "400", description = "요청 값 검증 실패", content = @Content),
      @ApiResponse(responseCode = "409", description = "중복된 이메일", content = @Content)
  })
  ResponseEntity<CommonApiResponse<AuthResponse>> signup(SignupRequest signupRequest);

  @Operation(summary = "로그인", description = "이메일과 비밀번호로 로그인하고 access token을 발급합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "로그인 성공"),
      @ApiResponse(responseCode = "400", description = "요청 값 검증 실패", content = @Content),
      @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content)
  })
  ResponseEntity<CommonApiResponse<AuthResponse>> login(LoginRequest loginRequest);

  @Operation(summary = "토큰 재발급", description = "refresh token 쿠키를 기반으로 access token을 재발급합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "토큰 재발급 성공"),
      @ApiResponse(responseCode = "401", description = "유효하지 않은 refresh token", content = @Content)
  })
  ResponseEntity<CommonApiResponse<AuthResponse>> refreshToken(String refreshToken);

  @Operation(summary = "로그아웃", description = "서버의 refresh token을 무효화하고 쿠키를 제거합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "로그아웃 성공"),
      @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content)
  })
  ResponseEntity<CommonApiResponse<Void>> logout(UserDetailsImpl userDetails, String refreshToken);
}



