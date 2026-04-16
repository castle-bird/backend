package io.project.backend.domain.employee.controller;

import io.project.backend.domain.employee.controller.api.EmployeeControllerApi;
import io.project.backend.domain.employee.dto.common.DepartmentListDto;
import io.project.backend.domain.employee.dto.common.PositionListDto;
import io.project.backend.domain.employee.dto.request.UpdateEmployeeRequest;
import io.project.backend.domain.employee.dto.response.DepartmentListResponse;
import io.project.backend.domain.employee.dto.response.PositionListResponse;
import io.project.backend.domain.employee.service.EmployeeService;
import io.project.backend.global.response.CommonApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
      @PathVariable Long id,
      @Valid @RequestBody UpdateEmployeeRequest request
  ) {

    employeeService.updateEmployee(id, request);

    return ResponseEntity
        .status(HttpStatus.NO_CONTENT)
        .body(CommonApiResponse.ok(null));
  }
}
