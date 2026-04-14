package io.project.backend.domain.auth.service.impl;

import io.jsonwebtoken.Claims;
import io.project.backend.domain.auth.dto.common.AuthTokenDto;
import io.project.backend.domain.auth.dto.request.LoginRequest;
import io.project.backend.domain.auth.dto.request.SignupRequest;
import io.project.backend.domain.auth.exception.AuthenticationException;
import io.project.backend.domain.auth.exception.InvalidTokenException;
import io.project.backend.domain.auth.exception.TokenReuseDetectedException;
import io.project.backend.domain.auth.repository.RefreshTokenRedisRepository;
import io.project.backend.domain.auth.service.AuthService;
import io.project.backend.domain.employee.entity.Department;
import io.project.backend.domain.employee.entity.Employee;
import io.project.backend.domain.employee.exception.DepartmentNotFoundException;
import io.project.backend.domain.employee.exception.EmployeeDuplicateException;
import io.project.backend.domain.employee.mapper.EmployeeMapper;
import io.project.backend.domain.employee.repository.DepartmentRepository;
import io.project.backend.domain.employee.repository.EmployeeRepository;
import io.project.backend.global.security.jwt.JwtProperties;
import io.project.backend.global.security.jwt.JwtProvider;
import io.project.backend.global.security.jwt.TokenType;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

  private final EmployeeRepository employeeRepository;
  private final DepartmentRepository departmentRepository;
  private final EmployeeMapper employeeMapper;
  private final PasswordEncoder passwordEncoder;
  private final JwtProvider jwtProvider;
  private final RefreshTokenRedisRepository refreshTokenRedisRepository;
  private final JwtProperties jwtProperties;

  @Override
  @Transactional
  public AuthTokenDto signup(SignupRequest request) {

    // 이메일 중복 체크
    String email = request.email();
    if (employeeRepository.existsByEmail(email)) {
      throw new EmployeeDuplicateException(Map.of("email", email));
    }

    // 부서 체크
    Department department = departmentRepository.findByName(request.department())
        .orElseThrow(() -> new DepartmentNotFoundException(
            Map.of("department", request.department())
        ));

    // 사원번호(날짜 + 입사자 수)생성
    LocalDate today = LocalDate.now();
    long hireDateCount = employeeRepository.countByHireDate(today) + 1;
    String employeeNumber = today.format(DateTimeFormatter.ofPattern("yyMMdd"))
                            + String.format("%02d", hireDateCount);

    // 사원 생성 및 저장
    Employee employee = employeeMapper.toEntityForSignup(
        request,
        passwordEncoder.encode(request.password()),
        employeeNumber,
        today,
        department
    );

    return issueAuthTokens(employeeRepository.save(employee));
  }

  @Override
  @Transactional
  public AuthTokenDto login(LoginRequest loginRequest) {

    // 직원 조회
    String email = loginRequest.email();
    Employee employee = employeeRepository.findByEmailAndDeletedFalse(email)
        .orElseThrow(() -> new AuthenticationException(
            Map.of("invalid", "이메일 또는 비밀번호가 잘못되었습니다.")
        ));

    // 비밀번호 검증
    if (!passwordEncoder.matches(
        loginRequest.password(),
        employee.getPassword())
    ) {
      throw new AuthenticationException(
          Map.of("invalid", "이메일 또는 비밀번호가 잘못되었습니다.")
      );
    }

    return issueAuthTokens(employee);
  }

  @Override
  @Transactional
  public AuthTokenDto refreshToken(String refreshToken) {
    // 토큰 존재 여부
    if (refreshToken == null || refreshToken.isBlank()) {
      throw new InvalidTokenException(
          Map.of("refreshToken", "refresh token이 없습니다.")
      );
    }

    // 토큰 파싱:
    // jjwt 0.13.0 기준 → 이 단계에서 서명검증, 유효기간 검증이 모두 이루어진다.
    // (예외 발생 시, 토큰이 유효하지 않거나 만료된 것이다.)
    Claims claims = jwtProvider.extractClaims(refreshToken);
    Long userId = Long.valueOf(claims.getSubject());
    String type = claims.get("type", String.class);
    String issuer = claims.getIssuer();

    // 타입 검증
    if (!TokenType.REFRESH_TOKEN.getValue().equals(type)) {
      throw new InvalidTokenException(
          Map.of("refreshToken", "refresh token이 아닙니다.")
      );
    }

    // 발행자 검증
    if (!jwtProperties.jwtIssuer().equals(issuer)) {
      throw new InvalidTokenException(
          Map.of("refreshToken", "issuer가 올바르지 않습니다.")
      );
    }

    // 토큰 조회
    String savedRefreshToken = refreshTokenRedisRepository.findByUserId(userId);
    if (savedRefreshToken == null) {
      throw new InvalidTokenException(
          Map.of("refreshToken", "저장된 refresh token이 없습니다.")
      );
    }

    // 토큰 재사용 감지:
    // 토큰의 사용자가 같은데, 토큰이 다른다?
    // → 이전 토큰이 탈취되어 재사용되고 있을 가능성이 있다.
    // → 사용자가 다른 기기에서 로그인하여 토큰이 갱신되었을 수 있다.
    if (!savedRefreshToken.equals(refreshToken)) {
      refreshTokenRedisRepository.delete(userId);
      throw new TokenReuseDetectedException(
          Map.of("refreshToken", "토큰 재사용이 감지되었습니다.")
      );
    }

    // 사용자 조회:
    // 토큰이 유효하다고 해도, 해당 사용자가 존재하지 않을 수 있다. (예: 탈퇴한 사용자)
    Employee employee = employeeRepository.findByIdAndDeletedFalse(userId)
        .orElseThrow(() -> new InvalidTokenException(
            Map.of("refreshToken", "토큰에 해당하는 사용자가 없습니다.")
        ));

    return issueAuthTokens(employee);
  }

  @Override
  @Transactional
  public void logout(Long userId) {
    // Redis에서 refresh token 삭제 → 해당 토큰은 더 이상 사용할 수 없다.
    refreshTokenRedisRepository.delete(userId);
  }

  /**
   * 전달된 employee에 대해 새로운 인증 토큰(access token, refresh token)을 발급한다.
   * refresh token은 Redis 저장소에 TTL과 함께 저장한다.
   *
   * @param employee 인증 토큰을 발급할 대상 employee
   * @return 새로 발급된 access token과 refresh token을 담은 {@code AuthTokenDto}
   */
  private AuthTokenDto issueAuthTokens(Employee employee) {
    String accessToken = jwtProvider.generateAccessToken(employee);
    String refreshToken = jwtProvider.generateRefreshToken(employee);
    Duration refreshTokenTtl = Duration.ofMillis(jwtProperties.refreshTokenExpiration());

    refreshTokenRedisRepository.save(
        employee.getId(),
        refreshToken,
        refreshTokenTtl
    );

    return AuthTokenDto.of(accessToken, refreshToken);
  }
}
