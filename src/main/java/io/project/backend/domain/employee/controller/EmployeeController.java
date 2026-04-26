package io.project.backend.domain.employee.controller;

import io.project.backend.domain.employee.controller.api.EmployeeControllerApi;
import io.project.backend.domain.employee.dto.common.DepartmentListDto;
import io.project.backend.domain.employee.dto.common.PositionListDto;
import io.project.backend.domain.employee.dto.request.UpdateEmployeeRequest;
import io.project.backend.domain.employee.dto.response.DepartmentListResponse;
import io.project.backend.domain.employee.dto.response.EmployeeListResponse;
import io.project.backend.domain.employee.dto.response.EmployeeResponse;
import io.project.backend.domain.employee.dto.response.PositionListResponse;
import io.project.backend.domain.employee.service.EmployeeService;
import io.project.backend.global.response.CommonApiResponse;
import io.project.backend.global.security.details.UserDetailsImpl;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
@Slf4j
public class EmployeeController implements EmployeeControllerApi {

  private final EmployeeService employeeService;

  @Override
  @GetMapping("/departments")
  public ResponseEntity<CommonApiResponse<List<DepartmentListResponse>>> getDepartmentList() {

    List<DepartmentListDto> results = employeeService.getDepartmentList();

    List<DepartmentListResponse> response = results.stream()
        .map(d -> DepartmentListResponse.from(d.name())).toList();

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(CommonApiResponse.ok(response));
  }

  @GetMapping("/positions")
  public ResponseEntity<CommonApiResponse<List<PositionListResponse>>> getPositionList() {
    List<PositionListDto> results = employeeService.getPositionList();

    List<PositionListResponse> response = results.stream()
        .map(d -> PositionListResponse.from(d.name())).toList();

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(CommonApiResponse.ok(response));
  }

  @Override
  @DeleteMapping("/{id}")
  public ResponseEntity<CommonApiResponse<Void>> deleteEmployee(
      @PathVariable Long id
  ) {
    employeeService.deleteEmployee(id);

    return ResponseEntity
        .status(HttpStatus.NO_CONTENT)
        .body(CommonApiResponse.ok(null));
  }

  @Override
  @PutMapping("/{id}")
  public ResponseEntity<CommonApiResponse<Void>> updateEmployee(
      @AuthenticationPrincipal UserDetailsImpl userDetails,
      @PathVariable Long id,
      @Valid @RequestBody UpdateEmployeeRequest request
  ) {

    employeeService.updateEmployee(userDetails.getUserId() ,id, request);

    return ResponseEntity
        .status(HttpStatus.NO_CONTENT)
        .body(CommonApiResponse.ok(null));
  }

  @Override
  @GetMapping("/{id}")
  public ResponseEntity<CommonApiResponse<EmployeeResponse>> getEmployee(@PathVariable Long id) {

    EmployeeResponse employeeResponse = employeeService.getEmployee(id);

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(CommonApiResponse.ok(employeeResponse));
  }

  @Override
  @GetMapping
  public ResponseEntity<CommonApiResponse<EmployeeListResponse>> getEmployeeList(
      @RequestParam(required = false) String department,
      @RequestParam(required = false) String position,
      @RequestParam(required = false) Long cursor,
      @RequestParam(defaultValue = "20") int size
  ) {

    EmployeeListResponse employeeListResponse = employeeService.getEmployeeList(
        department,
        position,
        cursor,
        size
    );

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(CommonApiResponse.ok(employeeListResponse));
  }

  @Override
  @GetMapping("/me")
  public ResponseEntity<CommonApiResponse<EmployeeResponse>> getMe(
      @AuthenticationPrincipal UserDetailsImpl userDetails
  ) {

    EmployeeResponse employeeResponse = employeeService.getMe(userDetails);

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(CommonApiResponse.ok(employeeResponse));
  }
}
