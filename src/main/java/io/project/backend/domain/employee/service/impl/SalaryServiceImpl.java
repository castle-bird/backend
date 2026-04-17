package io.project.backend.domain.employee.service.impl;

import io.project.backend.domain.employee.dto.request.SalaryRegisterRequest;
import io.project.backend.domain.employee.dto.request.SalaryUpdateRequest;
import io.project.backend.domain.employee.dto.response.SalaryResponse;
import io.project.backend.domain.employee.entity.Employee;
import io.project.backend.domain.employee.entity.Salary;
import io.project.backend.domain.employee.exception.EmployeeNotFoundException;
import io.project.backend.domain.employee.exception.SalaryDuplicateException;
import io.project.backend.domain.employee.mapper.SalaryMapper;
import io.project.backend.domain.employee.repository.EmployeeRepository;
import io.project.backend.domain.employee.repository.SalaryRepository;
import io.project.backend.domain.employee.service.SalaryService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SalaryServiceImpl implements SalaryService {

  private final SalaryRepository salaryRepository;
  private final EmployeeRepository employeeRepository;
  private final SalaryMapper salaryMapper;

  @Override
  @Transactional
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public void registerSalary(SalaryRegisterRequest request) {
    Employee employee = employeeRepository.findByIdAndDeletedFalse(request.employeeId())
        .orElseThrow(
            () -> new EmployeeNotFoundException(Map.of("employeeId", request.employeeId())));

    // 중복 급여 등록 방지
    // SQL에 unique 제약 조건이 있지만, 먼저 체크하여 예외를 던지는 것이 더 명확한 에러 메시지를 제공할 수 있음
    if (salaryRepository.findByEmployeeIdAndEmployeeDeletedIsFalse(employee.getId()).isPresent()) {
      throw new SalaryDuplicateException(Map.of("employeeId", employee.getId()));
    }

    Salary salary = salaryMapper.toEntityForRegister(employee, request);

    // 다시 한 번 중복 체크 (동시성 문제 방지)
    try {
      salaryRepository.save(salary);
    } catch (DataIntegrityViolationException e) {
      throw new SalaryDuplicateException(Map.of("employeeId", employee.getId()));
    }
  }

  @Override
  @Transactional(readOnly = true)
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public SalaryResponse getSalary(Long employeeId) {
    Salary salary = salaryRepository.findByEmployeeIdAndEmployeeDeletedIsFalse(employeeId)
        .orElseThrow(
            () -> new EmployeeNotFoundException(Map.of("employeeId", employeeId)));

    return salaryMapper.toResponse(salary);
  }

  @Override
  @Transactional(readOnly = true)
  public SalaryResponse getSalaryMe(UserDetails userDetails) {
    Employee employee = employeeRepository.findByEmailAndDeletedFalse(userDetails.getUsername())
        .orElseThrow(
            () -> new EmployeeNotFoundException(Map.of("email", userDetails.getUsername())));

    Salary salary = salaryRepository.findByEmployeeIdAndEmployeeDeletedIsFalse(employee.getId())
        .orElseThrow(
            () -> new EmployeeNotFoundException(Map.of("employeeId", employee.getId())));

    return salaryMapper.toResponse(salary);
  }

  @Override
  @Transactional
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public void updateSalary(Long employeeId, SalaryUpdateRequest request) {
    Salary salary = salaryRepository.findByEmployeeIdAndEmployeeDeletedIsFalse(employeeId)
        .orElseThrow(
            () -> new EmployeeNotFoundException(Map.of("employeeId", employeeId)));

    salary.updateSalary(request.monthSalary(), request.yearSalary());
    salary.updatePaymentDay(request.paymentDay());

    salaryRepository.save(salary);
  }
}
