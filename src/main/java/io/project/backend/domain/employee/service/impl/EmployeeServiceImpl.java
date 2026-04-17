package io.project.backend.domain.employee.service.impl;

import io.project.backend.domain.auth.repository.RefreshTokenRedisRepository;
import io.project.backend.domain.employee.dto.common.DepartmentListDto;
import io.project.backend.domain.employee.dto.common.PositionListDto;
import io.project.backend.domain.employee.dto.request.UpdateEmployeeRequest;
import io.project.backend.domain.employee.dto.response.EmployeeListResponse;
import io.project.backend.domain.employee.dto.response.EmployeeResponse;
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
import io.project.backend.domain.employee.service.EmployeeService;
import io.project.backend.global.security.details.UserDetailsImpl;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

  private final String ADMIN_POSITION_NAME = "시스템 관리자";

  private final EmployeeRepository employeeRepository;
  private final DepartmentRepository departmentRepository;
  private final EmployeePositionRepository employeePositionRepository;
  private final RefreshTokenRedisRepository refreshTokenRedisRepository;
  private final EmployeeMapper employeeMapper;

  @Override
  @Transactional(readOnly = true)
  public List<DepartmentListDto> getDepartmentList() {
    return departmentRepository.findAll().stream()
        .map(department -> DepartmentListDto.from(department.getName())).toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<PositionListDto> getPositionList() {
    return employeePositionRepository.findAllByNameIsNot(ADMIN_POSITION_NAME).stream()
        .map(position -> PositionListDto.from(position.getName())).toList();
  }

  @Override
  @Transactional
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public void deleteEmployee(Long id) {
    Employee employee = employeeRepository.findByIdAndDeletedFalse(id)
        .orElseThrow(() -> new EmployeeNotFoundException(Map.of("userId", id)));

    refreshTokenRedisRepository.deleteAllByUserId(employee.getId());

    employee.softDelete();
  }

  @Override
  @Transactional
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public void updateEmployee(Long id, UpdateEmployeeRequest request) {

    Employee employee = employeeRepository.findByIdAndDeletedFalse(id)
        .orElseThrow(() -> new EmployeeNotFoundException(Map.of("userId", id)));

    // 이메일 중복 체크 (현재 이메일과 다르면서, 이미 존재하는 이메일인 경우)
    if (!employee.getEmail().equals(request.email())
        && employeeRepository.existsByEmail(request.email())) {
      throw new EmployeeDuplicateException(Map.of("email", request.email()));
    }

    // 부서 및 직급 존재 여부 체크
    Department department = departmentRepository.findByName(request.department())
        .orElseThrow(
            () -> new DepartmentNotFoundException(Map.of("departmentName", request.department())));

    EmployeePosition employeePosition = employeePositionRepository.findByName(
            request.employeePosition())
        .orElseThrow(() -> new PositionNotFoundException(
            Map.of("employeePositionName", request.employeePosition())));

    // 직원 정보 업데이트
    employee.updateAllInfo(
        request,
        department,
        employeePosition
    );

    // 변경된 직원 정보 저장
    employeeRepository.save(employee);
  }

  @Override
  @Transactional(readOnly = true)
  public EmployeeResponse getEmployee(Long id) {

    Employee find = employeeRepository.findByIdAndDeletedFalse(id)
        .orElseThrow(() -> new EmployeeNotFoundException(Map.of("userId", id)));

    return employeeMapper.toEmployeeResponse(find);
  }

  @Override
  @Transactional(readOnly = true)
  public EmployeeListResponse getEmployeeList(
      String department,
      String position,
      Long cursor,
      int size
  ) {

    List<Employee> employees = employeeRepository.findAllByCursor(
        department,
        position,
        cursor,
        size
    );

    boolean hasNext = employees.size() > size;
    List<EmployeeResponse> responses = employees.stream()
        .limit(hasNext ? size : employees.size())
        .map(employeeMapper::toEmployeeResponse)
        .toList();

    return EmployeeListResponse.from(responses,  hasNext);
  }

  @Override
  @Transactional(readOnly = true)
  public EmployeeResponse getMe(UserDetailsImpl userDetails) {

    Long userId = userDetails.getUserId();

    Employee find = employeeRepository.findByIdAndDeletedFalse(userId)
        .orElseThrow(() -> new EmployeeNotFoundException(Map.of("userId", userId)));

    return employeeMapper.toEmployeeResponse(find);
  }
}
