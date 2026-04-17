package io.project.backend.domain.employee.controller.api;

import io.project.backend.domain.employee.dto.request.SalaryRegisterRequest;
import io.project.backend.domain.employee.dto.request.SalaryUpdateRequest;
import io.project.backend.domain.employee.dto.response.SalaryResponse;
import io.project.backend.global.response.CommonApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

public interface SalaryControllerApi {

  ResponseEntity<CommonApiResponse<Void>> registerSalary(SalaryRegisterRequest request);

  ResponseEntity<CommonApiResponse<SalaryResponse>> getSalary(Long employeeId);

  ResponseEntity<CommonApiResponse<SalaryResponse>> getSalaryMe(UserDetails userDetails);

  ResponseEntity<CommonApiResponse<Void>> updateSalary(Long employeeId, SalaryUpdateRequest request);
}
