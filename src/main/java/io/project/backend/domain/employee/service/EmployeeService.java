package io.project.backend.domain.employee.service;

import io.project.backend.domain.employee.dto.common.DepartmentListDto;
import io.project.backend.domain.employee.dto.common.PositionListDto;
import java.util.List;

public interface EmployeeService {
  List<DepartmentListDto> getDepartmentList();

  List<PositionListDto> getPositionList();
}
