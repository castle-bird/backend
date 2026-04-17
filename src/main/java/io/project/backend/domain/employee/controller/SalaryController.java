package io.project.backend.domain.employee.controller;

import io.project.backend.domain.employee.controller.api.SalaryControllerApi;
import io.project.backend.domain.employee.dto.request.SalaryRegisterRequest;
import io.project.backend.domain.employee.dto.response.SalaryResponse;
import io.project.backend.global.response.CommonApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

public class SalaryController implements SalaryControllerApi {

  @Override
  public ResponseEntity<CommonApiResponse<Void>> registerSalary(SalaryRegisterRequest request) {
    return null;
  }

  @Override
  public ResponseEntity<CommonApiResponse<SalaryResponse>> getSalary(Long employeeId) {
    return null;
  }

  @Override
  public ResponseEntity<CommonApiResponse<SalaryResponse>> getSalaryMe(UserDetails userDetails) {
    return null;
  }

  @Override
  public ResponseEntity<CommonApiResponse<Void>> updateSalary(Long id,
      SalaryRegisterRequest request) {
    return null;
  }

  @Override
  public ResponseEntity<CommonApiResponse<Void>> updateSalaryBonus(Long id,
      SalaryRegisterRequest request) {
    return null;
  }
}
