package io.project.backend.domain.dashboard.batch.impl;

import io.project.backend.domain.dashboard.batch.DashboardBatchService;
import io.project.backend.domain.dashboard.repository.DashboardDailyStatsRepository;
import io.project.backend.domain.dashboard.repository.DashboardDepartmentStatsRepository;
import io.project.backend.domain.employee.repository.DepartmentRepository;
import io.project.backend.domain.employee.repository.EmployeeRepository;
import io.project.backend.domain.reservation.repository.RoomReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DashboardBatchServiceImpl implements DashboardBatchService {

  private final DashboardDailyStatsRepository dailyStatsRepository;
  private final DashboardDepartmentStatsRepository departmentStatsRepository;
  private final EmployeeRepository employeeRepository;
  private final DepartmentRepository departmentRepository;
  private final RoomReservationRepository roomReservationRepository;

  @Override
  @Transactional
  public void aggregateDailyStats() {

  }

  @Override
  @Transactional
  public void aggregateDepartmentStats() {
    
  }
}
