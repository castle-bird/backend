package io.project.backend.global.config;

import io.project.backend.domain.employee.entity.Employee;
import io.project.backend.domain.employee.entity.EmployeeRole;
import io.project.backend.domain.employee.repository.EmployeeRepository;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
@EnableConfigurationProperties(AdminProperties.class)
public class DataInitializer implements CommandLineRunner {

  private static final String ADMIN_EMPLOYEE_NUMBER = "ADMIN001";
  private static final String ADMIN_POSITION = "시스템 관리자";

  private final EmployeeRepository employeeRepository;
  private final PasswordEncoder passwordEncoder;
  private final AdminProperties adminProperties;

  @Override
  @Transactional
  public void run(String... args) {
    if (employeeRepository.existsByEmail(adminProperties.email())) {
      log.debug("관리자 계정이 이미 존재합니다. 초기화를 건너뜁니다.");
      return;
    }

    String encodedPassword = passwordEncoder.encode(adminProperties.password());

    Employee admin = Employee.builder()
        .employeeNumber(ADMIN_EMPLOYEE_NUMBER)
        .name(adminProperties.name())
        .email(adminProperties.email())
        .password(encodedPassword)
        .role(EmployeeRole.ADMIN)
        .position(ADMIN_POSITION)
        .department(null)
        .hireDate(LocalDate.now())
        .build();

    // passwordChangeRequired = false (초기 관리자는 비밀번호 변경 강제 불필요)
    admin.changePassword(encodedPassword);

    employeeRepository.save(admin);

    log.info("초기 관리자 계정이 생성되었습니다. 이메일: {}", adminProperties.email());
  }
}
