package io.project.backend.domain.employee.service.impl;

import io.project.backend.domain.employee.dto.common.DepartmentListDto;
import io.project.backend.domain.employee.repository.DepartmentRepository;
import io.project.backend.domain.employee.service.EmployeeService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

  private final DepartmentRepository departmentRepository;

  @Override
  @Transactional(readOnly = true)
  public List<DepartmentListDto> getDepartmentList() {
    return departmentRepository.findAll().stream()
        .map(department -> DepartmentListDto.from(department.getName())).toList();
  }
}
