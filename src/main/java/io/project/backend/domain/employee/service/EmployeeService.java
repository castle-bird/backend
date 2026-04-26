package io.project.backend.domain.employee.service;

import io.project.backend.domain.employee.dto.common.DepartmentListDto;
import io.project.backend.domain.employee.dto.common.PositionListDto;
import io.project.backend.domain.employee.dto.request.UpdateEmployeeRequest;
import io.project.backend.domain.employee.dto.response.EmployeeListResponse;
import io.project.backend.domain.employee.dto.response.EmployeeResponse;
import io.project.backend.global.security.details.UserDetailsImpl;
import java.util.List;

public interface EmployeeService {

  List<DepartmentListDto> getDepartmentList();

  List<PositionListDto> getPositionList();

  void deleteEmployee(Long id);

  void updateEmployee(Long adminId, Long id, UpdateEmployeeRequest request);

  EmployeeResponse getEmployee(Long id);

  EmployeeListResponse getEmployeeList(
      String department,
      String position,
      Long cursor,
      int size
  );

  EmployeeResponse getMe(UserDetailsImpl userDetails);
}
