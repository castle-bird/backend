package io.project.backend.domain.dashboard.mapper;

import io.project.backend.domain.dashboard.dto.response.DashboardDailyStatsResponse;
import io.project.backend.domain.dashboard.entity.DashboardDailyStats;
import java.time.LocalDate;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DashboardDailyStatsMapper {

  DashboardDailyStats toEntity(
      LocalDate snapshotDate,
      int totalEmployees,
      int newHiresThisMonth,
      int managerCount,
      int employeeCount
  );


  DashboardDailyStatsResponse toResponse(DashboardDailyStats entity);
}
