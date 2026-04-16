package io.project.backend.domain.employee.service;

import io.project.backend.domain.employee.dto.common.DepartmentListDto;
import io.project.backend.domain.employee.dto.common.PositionListDto;
import io.project.backend.domain.employee.dto.request.UpdateEmployeeRequest;
import java.util.List;

public interface EmployeeService {

  List<DepartmentListDto> getDepartmentList();

  List<PositionListDto> getPositionList();

  void deleteEmployee(Long id);

  void updateEmployee(Long id, UpdateEmployeeRequest request);
}
