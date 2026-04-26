package io.project.backend.domain.dashboard.service.impl;

import io.project.backend.domain.dashboard.dto.response.DashboardDailyStatsResponse;
import io.project.backend.domain.dashboard.dto.response.DepartmentStatResponse;
import io.project.backend.domain.dashboard.entity.DashboardDailyStats;
import io.project.backend.domain.dashboard.entity.DashboardDepartmentStats;
import io.project.backend.domain.dashboard.mapper.DashboardDailyStatsMapper;
import io.project.backend.domain.dashboard.mapper.DashboardDepartmentStatsMapper;
import io.project.backend.domain.dashboard.repository.DashboardDailyStatsRepository;
import io.project.backend.domain.dashboard.repository.DashboardDepartmentStatsRepository;
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
  private final DashboardDailyStatsMapper dashboardDailyStatsMapper;
  private final DashboardDepartmentStatsMapper dashboardDepartmentStatsMapper;

  @Override
  @Transactional(readOnly = true)
  public List<DashboardDailyStatsResponse> getDailyStats() {

    List<DashboardDailyStats> result = dailyStatsRepository.findAll();

    return result.stream()
        .map(dashboardDailyStatsMapper::toResponse)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<DepartmentStatResponse> getDepartmentStats() {

    List<DashboardDepartmentStats> result = departmentStatsRepository.findAll();

    return result.stream()
        .map(dashboardDepartmentStatsMapper::toResponse)
        .toList();
  }
}
