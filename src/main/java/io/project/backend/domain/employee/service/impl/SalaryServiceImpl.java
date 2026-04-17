package io.project.backend.domain.employee.service.impl;

import io.project.backend.domain.employee.dto.request.SalaryRegisterRequest;
import io.project.backend.domain.employee.dto.request.SalaryUpdateRequest;
import io.project.backend.domain.employee.dto.response.SalaryResponse;
import io.project.backend.domain.employee.entity.Employee;
import io.project.backend.domain.employee.entity.Salary;
import io.project.backend.domain.employee.exception.EmployeeNotFoundException;
import io.project.backend.domain.employee.mapper.SalaryMapper;
import io.project.backend.domain.employee.repository.EmployeeRepository;
import io.project.backend.domain.employee.repository.SalaryRepository;
import io.project.backend.domain.employee.service.SalaryService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    Salary salary = salaryMapper.toEntityForRegister(employee, request);

    salaryRepository.save(salary);
  }

  @Override
  @Transactional(readOnly = true)
  public SalaryResponse getSalary(Long employeeId) {
    return null;
  }

  @Override
  @Transactional(readOnly = true)
  public SalaryResponse getSalaryMe(UserDetails userDetails) {
    return null;
  }

  @Override
  @Transactional
  public void updateSalary(Long employeeId, SalaryUpdateRequest request) {

  }
}
