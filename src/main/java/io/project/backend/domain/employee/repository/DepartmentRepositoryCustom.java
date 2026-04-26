package io.project.backend.domain.employee.repository;

import io.project.backend.domain.employee.dto.common.DashboardDepartmentStatsDto;
import java.util.List;

public interface DepartmentRepositoryCustom {

  List<DashboardDepartmentStatsDto> getDepartmentStats();
}
