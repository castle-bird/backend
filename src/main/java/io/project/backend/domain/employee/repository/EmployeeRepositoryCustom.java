package io.project.backend.domain.employee.repository;

import io.project.backend.domain.employee.entity.Employee;
import java.util.List;

public interface EmployeeRepositoryCustom {

  List<Employee> findAllByCursor(
      String department,
      String position,
      Long cursor,
      int size
  );
}
