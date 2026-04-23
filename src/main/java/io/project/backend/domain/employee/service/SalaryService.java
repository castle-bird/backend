package io.project.backend.domain.employee.service;

import io.project.backend.domain.employee.dto.request.SalaryRegisterRequest;
import io.project.backend.domain.employee.dto.request.SalaryUpdateRequest;
import io.project.backend.domain.employee.dto.response.SalaryResponse;
import org.springframework.security.core.userdetails.UserDetails;

public interface SalaryService {

  void registerSalary(SalaryRegisterRequest request);

  SalaryResponse getSalary(Long employeeId);

  SalaryResponse getSalaryMe(UserDetails userDetails);

  void updateSalary(Long adminId, Long employeeId, SalaryUpdateRequest request);
}
