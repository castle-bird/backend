package io.project.backend.domain.dashboard.mapper;

import io.project.backend.domain.dashboard.dto.response.DepartmentStatResponse;
import io.project.backend.domain.dashboard.entity.DashboardDepartmentStats;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DashboardDepartmentStatsMapper {

  DepartmentStatResponse toResponse(DashboardDepartmentStats entity);
}
