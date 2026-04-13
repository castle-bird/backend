package io.project.backend.domain.auth.service.impl;

import io.project.backend.domain.auth.dto.common.AuthTokenDto;
import io.project.backend.domain.auth.dto.request.SignupRequest;
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

    Employee saved = employeeRepository.save(employee);

    String accessToken = jwtProvider.generateAccessToken(saved);
    String refreshToken = jwtProvider.generateRefreshToken(saved);
    Duration refreshTokenTtl = Duration.ofMillis(jwtProperties.refreshTokenExpiration());

    refreshTokenRedisRepository.save(
        saved.getId(),
        refreshToken,
        refreshTokenTtl
    );

    return AuthTokenDto.of(accessToken, refreshToken);
  }
}
