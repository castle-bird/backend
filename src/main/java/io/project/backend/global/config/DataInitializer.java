package io.project.backend.global.config;

import io.project.backend.domain.employee.entity.Department;
import io.project.backend.domain.employee.entity.Employee;
import io.project.backend.domain.employee.entity.EmployeePosition;
import io.project.backend.domain.employee.entity.EmployeeRole;
import io.project.backend.domain.employee.repository.DepartmentRepository;
import io.project.backend.domain.employee.repository.EmployeePositionRepository;
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
  private static final String MANAGEMENT_SUPPORT_TEAM = "경영지원팀";
  private static final String DEFAULT_ADMIN_ADDRESS = "서울시 강남구";
  private static final String DEFAULT_ADMIN_PHONE = "010-1234-1004";
  private static final short DEFAULT_POSITION_SORT_ORDER = 1;

  private final DepartmentRepository departmentRepository;
  private final EmployeeRepository employeeRepository;
  private final EmployeePositionRepository employeePositionRepository;
  private final PasswordEncoder passwordEncoder;
  private final AdminProperties adminProperties;

  @Override
  @Transactional
  public void run(String... args) {
    if (employeeRepository.existsByEmail(adminProperties.email())) {
      log.debug("관리자 계정이 이미 존재합니다. 초기화를 건너뜁니다.");
      return;
    }

    Department department = departmentRepository.findByName(MANAGEMENT_SUPPORT_TEAM).orElse(null);
    EmployeePosition employeePosition = getOrCreateAdminPosition();
    String encodedPassword = passwordEncoder.encode(adminProperties.password());

    Employee admin = Employee.builder()
        .employeeNumber(ADMIN_EMPLOYEE_NUMBER)
        .name(adminProperties.name())
        .email(adminProperties.email())
        .password(encodedPassword)
        .role(EmployeeRole.ADMIN)
        .employeePosition(employeePosition)
        .department(department)
        .address(DEFAULT_ADMIN_ADDRESS)
        .phone(DEFAULT_ADMIN_PHONE)
        .hireDate(LocalDate.now())
        .build();

    admin.changePassword(encodedPassword);

    employeeRepository.save(admin);

    log.info("초기 관리자 계정을 생성했습니다. email={}", adminProperties.email());
  }

  private EmployeePosition getOrCreateAdminPosition() {
    return employeePositionRepository.findByName(ADMIN_POSITION)
        .orElseGet(() -> {
          short nextSortOrder = employeePositionRepository.findTopByOrderBySortOrderDesc()
              .map(position -> (short) (position.getSortOrder() + 1))
              .orElse(DEFAULT_POSITION_SORT_ORDER);
          EmployeePosition created = employeePositionRepository.save(EmployeePosition.builder()
              .name(ADMIN_POSITION)
              .sortOrder(nextSortOrder)
              .build());
          log.info("직급 '{}'이(가) 없어 자동 생성했습니다. sortOrder={}", ADMIN_POSITION, nextSortOrder);
          return created;
        });
  }
}
