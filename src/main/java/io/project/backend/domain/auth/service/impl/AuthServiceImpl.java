package io.project.backend.domain.auth.service.impl;

import io.jsonwebtoken.Claims;
import io.project.backend.domain.auth.dto.common.AuthTokenDto;
import io.project.backend.domain.auth.dto.common.LoginDto;
import io.project.backend.domain.auth.dto.request.LoginRequest;
import io.project.backend.domain.auth.dto.request.PasswordChangeRequest;
import io.project.backend.domain.auth.dto.request.SignupRequest;
import io.project.backend.domain.auth.dto.response.SignupResponse;
import io.project.backend.domain.auth.exception.AuthenticationException;
import io.project.backend.domain.auth.exception.InvalidTokenException;
import io.project.backend.domain.auth.repository.RefreshTokenRedisRepository;
import io.project.backend.domain.auth.service.AuthService;
import io.project.backend.domain.employee.entity.Department;
import io.project.backend.domain.employee.entity.Employee;
import io.project.backend.domain.employee.entity.EmployeePosition;
import io.project.backend.domain.employee.exception.DepartmentNotFoundException;
import io.project.backend.domain.employee.exception.EmployeeDuplicateException;
import io.project.backend.domain.employee.exception.EmployeeNotFoundException;
import io.project.backend.domain.employee.exception.PositionNotFoundException;
import io.project.backend.domain.employee.mapper.EmployeeMapper;
import io.project.backend.domain.employee.repository.DepartmentRepository;
import io.project.backend.domain.employee.repository.EmployeePositionRepository;
import io.project.backend.domain.employee.repository.EmployeeRepository;
import io.project.backend.global.security.jwt.JwtProperties;
import io.project.backend.global.security.jwt.JwtProvider;
import io.project.backend.global.security.jwt.TokenType;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

  private final EmployeeRepository employeeRepository;
  private final DepartmentRepository departmentRepository;
  private final EmployeePositionRepository employeePositionRepository;
  private final EmployeeMapper employeeMapper;
  private final PasswordEncoder passwordEncoder;
  private final JwtProvider jwtProvider;
  private final RefreshTokenRedisRepository refreshTokenRedisRepository;
  private final JwtProperties jwtProperties;

  @Override
  @Transactional
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public SignupResponse createEmployee(SignupRequest request) {

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

    // 직급 체크
    EmployeePosition employeePosition = employeePositionRepository.findByName(
            request.employeePosition())
        .orElseThrow(() -> new PositionNotFoundException(
            Map.of("employeePosition", request.employeePosition())));

    // 사원번호(날짜 + 입사자 수)생성
    LocalDate today = LocalDate.now();
    long hireDateCount = employeeRepository.countByHireDate(today) + 1;
    String employeeNumber = today.format(DateTimeFormatter.ofPattern("yyMMdd"))
                            + String.format("%02d", hireDateCount);

    // 비번 임시 생성 → 첫 로그인시 변경 해야함
    String temporaryPassword = generateTemporaryPassword();
    String encodedPassword = passwordEncoder.encode(temporaryPassword);

    // 사원 생성 및 저장
    Employee employee = employeeMapper.toEntityForSignup(
        request,
        encodedPassword,
        employeeNumber,
        today,
        department,
        employeePosition
    );

    employeeRepository.save(employee);

    return SignupResponse.form(employee.getEmail(), temporaryPassword);
  }

  @Override
  @Transactional
  public LoginDto login(LoginRequest loginRequest) {

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

    // 최초 접속 확인
    boolean passwordChangeRequired = employee.isPasswordChangeRequired();

    AuthTokenDto tokens = issueAuthTokens(employee);

    return LoginDto.from(tokens, passwordChangeRequired);
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
    String email = claims.getSubject();
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

    // 사용자 조회:
    // 토큰이 유효하다고 해도, 해당 사용자가 존재하지 않을 수 있다. (예: 탈퇴한 사용자)
    Employee employee = employeeRepository.findByEmailAndDeletedFalse(email)
        .orElseThrow(() -> new InvalidTokenException(
            Map.of("refreshToken", "토큰에 해당하는 사용자가 없습니다.")
        ));

    // 토큰 조회
    if (!refreshTokenRedisRepository.exists(employee.getId(), refreshToken)) {
      throw new InvalidTokenException(
          Map.of("refreshToken", "저장된 refresh token이 없습니다.")
      );
    }

    refreshTokenRedisRepository.delete(employee.getId(), refreshToken);
    return issueAuthTokens(employee);
  }

  @Override
  @Transactional
  public void logout(Long userId, String refreshToken) {
    if (refreshToken == null || refreshToken.isBlank()) {
      return;
    }

    // Redis에서 refresh token 삭제 → 해당 토큰은 더 이상 사용할 수 없다.
    refreshTokenRedisRepository.delete(userId, refreshToken);
  }

  /**
   * 영문자·숫자·특수문자(@$!%*#?&)를 각 1자 이상 포함한 12자리 임시 비밀번호를 생성한다.
   *
   * @return 패턴 제약을 만족하는 임시 비밀번호 문자열
   */
  private String generateTemporaryPassword() {
    final String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    final String digits = "0123456789";
    final String specials = "@$!%*#?&";
    final String allChars = letters + digits + specials;

    SecureRandom random = new SecureRandom();
    char[] password = new char[12];

    // 각 유형 최소 1자 보장
    password[0] = letters.charAt(random.nextInt(letters.length()));
    password[1] = digits.charAt(random.nextInt(digits.length()));
    password[2] = specials.charAt(random.nextInt(specials.length()));

    // 나머지 채우기
    for (int i = 3; i < password.length; i++) {
      password[i] = allChars.charAt(random.nextInt(allChars.length()));
    }

    // 섞기
    for (int i = password.length - 1; i > 0; i--) {
      int j = random.nextInt(i + 1); // +1 해야 자기 포함
      char tmp = password[i];

      password[i] = password[j];
      password[j] = tmp;
    }

    return new String(password);
  }

  @Override
  @Transactional
  public void passwordChange(Long userId, PasswordChangeRequest passwordChangeRequest) {
    // 사원 검증
    Employee employee = employeeRepository.findByIdAndDeletedFalse(userId).orElseThrow(
        () -> new EmployeeNotFoundException(Map.of("userId", userId))
    );

    // 현재 비밀번호 검증
    if (!passwordEncoder.matches(
        passwordChangeRequest.currentPwd(),
        employee.getPassword())
    ) {
      throw new AuthenticationException(
          Map.of("invalid", "비밀번호가 잘못되었습니다.")
      );
    }

    // 새 비밀번호가 현재 비밀번호와 동일한 경우 거부
    if (passwordEncoder.matches(passwordChangeRequest.newPwd(), employee.getPassword())) {
      throw new AuthenticationException(
          Map.of("invalid", "새 비밀번호는 현재 비밀번호와 달라야 합니다.")
      );
    }

    employee.changePassword(passwordEncoder.encode(passwordChangeRequest.newPwd()));
    refreshTokenRedisRepository.deleteAllByUserId(userId);
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

    Claims refreshTokenClaims = jwtProvider.extractClaims(refreshToken);
    String tokenId = refreshTokenClaims.getId();
    Instant issuedAt = refreshTokenClaims.getIssuedAt().toInstant();

    // 레디스 저장
    refreshTokenRedisRepository.save(
        employee.getId(),
        tokenId,
        refreshToken,
        issuedAt,
        refreshTokenTtl
    );

    return AuthTokenDto.of(accessToken, refreshToken);
  }
}
