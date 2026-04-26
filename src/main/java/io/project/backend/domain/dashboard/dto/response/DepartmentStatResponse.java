package io.project.backend.domain.dashboard.dto.response;

import io.project.backend.domain.dashboard.entity.DashboardDepartmentStats;

public record DepartmentStatResponse(
    String departmentName,
    int employeeCount,
    String managerName
) {

  public static DepartmentStatResponse from(DashboardDepartmentStats entity) {
    return new DepartmentStatResponse(
        entity.getDepartmentName(),
        entity.getEmployeeCount(),
        entity.getManagerName()
    );
  }
}
