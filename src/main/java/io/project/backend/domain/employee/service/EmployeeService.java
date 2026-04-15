package io.project.backend.domain.employee.service;

import io.project.backend.domain.employee.dto.common.DepartmentListDto;
import java.util.List;

public interface EmployeeService {
  List<DepartmentListDto> getDepartmentList();
}
