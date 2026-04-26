package io.project.backend.domain.employee.repository;

import io.project.backend.domain.employee.entity.Employee;
import io.project.backend.domain.employee.entity.EmployeeRole;
import java.time.LocalDate;
import java.util.List;

public interface EmployeeRepositoryCustom {

  List<Employee> findAllByCursor(
      String department,
      String position,
      Long cursor,
      int size
  );

  long countByHireDateBetween(LocalDate startDate, LocalDate endDate);

  long countByRole(EmployeeRole role);
}
