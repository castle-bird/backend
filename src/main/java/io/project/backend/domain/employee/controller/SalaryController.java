package io.project.backend.domain.employee.controller;

import io.project.backend.domain.employee.controller.api.SalaryControllerApi;
import io.project.backend.domain.employee.dto.request.SalaryRegisterRequest;
import io.project.backend.domain.employee.dto.request.SalaryUpdateRequest;
import io.project.backend.domain.employee.dto.response.SalaryResponse;
import io.project.backend.domain.employee.service.SalaryService;
import io.project.backend.global.response.CommonApiResponse;
import io.project.backend.global.security.details.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/salaries")
@RequiredArgsConstructor
public class SalaryController implements SalaryControllerApi {

  private final SalaryService salaryService;

  @Override
  @PostMapping("/register")
  public ResponseEntity<CommonApiResponse<Void>> registerSalary(
      @Valid @RequestBody SalaryRegisterRequest request
  ) {
    salaryService.registerSalary(request);

    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(CommonApiResponse.created(null));
  }

  @Override
  @GetMapping("/{employeeId}")
  public ResponseEntity<CommonApiResponse<SalaryResponse>> getSalary(
      @PathVariable Long employeeId
  ) {
    SalaryResponse response = salaryService.getSalary(employeeId);

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(CommonApiResponse.ok(response));
  }

  @Override
  @GetMapping("/me")
  public ResponseEntity<CommonApiResponse<SalaryResponse>> getSalaryMe(
      @AuthenticationPrincipal UserDetails userDetails
  ) {
    SalaryResponse response = salaryService.getSalaryMe(userDetails);

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(CommonApiResponse.ok(response));
  }

  @Override
  @PutMapping("/{employeeId}/salary")
  public ResponseEntity<CommonApiResponse<Void>> updateSalary(
      @AuthenticationPrincipal UserDetailsImpl userDetails,
      @PathVariable Long employeeId,
      @Valid @RequestBody SalaryUpdateRequest request
  ) {
    salaryService.updateSalary(userDetails.getUserId(), employeeId, request);

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(CommonApiResponse.ok(null));
  }
}
