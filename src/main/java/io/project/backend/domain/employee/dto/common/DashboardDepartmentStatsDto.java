package io.project.backend.domain.employee.dto.common;

import java.time.LocalDate;

public record DashboardDepartmentStatsDto(
    LocalDate snapshotDate,
    String departmentName,
    Long employeeCount,
    String managerName
) {

  public static DashboardDepartmentStatsDto of(
      String departmentName,
      Long employeeCount,
      String managerName
  ) {
    LocalDate now = LocalDate.now();

    return new DashboardDepartmentStatsDto(
        now,
        departmentName,
        employeeCount,
        managerName
    );
  }
}
