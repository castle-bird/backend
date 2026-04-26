package io.project.backend.domain.dashboard.service;

import io.project.backend.domain.dashboard.dto.response.DashboardDailyStatsResponse;
import io.project.backend.domain.dashboard.dto.response.DepartmentStatResponse;
import java.util.List;

public interface DashboardService {

  DashboardDailyStatsResponse getDailyStats();

  List<DepartmentStatResponse> getDepartmentStats();
}
