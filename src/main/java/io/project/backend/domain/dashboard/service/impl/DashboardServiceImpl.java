package io.project.backend.domain.dashboard.service.impl;

import io.project.backend.domain.dashboard.dto.response.DashboardDailyStatsResponse;
import io.project.backend.domain.dashboard.dto.response.DepartmentStatResponse;
import io.project.backend.domain.dashboard.repository.dailyStats.DashboardDailyStatsRepository;
import io.project.backend.domain.dashboard.repository.departmentStats.DashboardDepartmentStatsRepository;
import io.project.backend.domain.dashboard.service.DashboardService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

  private final DashboardDailyStatsRepository dailyStatsRepository;
  private final DashboardDepartmentStatsRepository departmentStatsRepository;

  @Override
  @Transactional(readOnly = true)
  public DashboardDailyStatsResponse getDailyStats() {

    return null;
  }

  @Override
  @Transactional(readOnly = true)
  public List<DepartmentStatResponse> getDepartmentStats() {

    return null;
  }
}
