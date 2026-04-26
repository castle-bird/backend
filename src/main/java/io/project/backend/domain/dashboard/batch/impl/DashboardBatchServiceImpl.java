package io.project.backend.domain.dashboard.batch.impl;

import io.project.backend.domain.dashboard.batch.DashboardBatchService;
import io.project.backend.domain.dashboard.entity.DashboardDailyStats;
import io.project.backend.domain.dashboard.entity.DashboardDepartmentStats;
import io.project.backend.domain.dashboard.mapper.DashboardDailyStatsMapper;
import io.project.backend.domain.dashboard.repository.dailyStats.DashboardDailyStatsRepository;
import io.project.backend.domain.dashboard.repository.departmentStats.DashboardDepartmentStatsRepository;
import io.project.backend.domain.employee.dto.common.DashboardDepartmentStatsDto;
import io.project.backend.domain.employee.entity.EmployeeRole;
import io.project.backend.domain.employee.repository.DepartmentRepository;
import io.project.backend.domain.employee.repository.EmployeeRepository;
import io.project.backend.domain.reservation.repository.RoomReservationRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DashboardBatchServiceImpl implements DashboardBatchService {

  private final DashboardDailyStatsMapper dashboardDailyStatsMapper;

  // 참조
  private final EmployeeRepository employeeRepository;
  private final DepartmentRepository departmentRepository;
  private final RoomReservationRepository roomReservationRepository;

  // 대시보드
  private final DashboardDailyStatsRepository dailyStatsRepository;
  private final DashboardDepartmentStatsRepository departmentStatsRepository;

  @Override
  @Transactional
  public void dailyStats() {
    LocalDate nowDate = LocalDate.now();

    int totalEmployees = (int) employeeRepository.countByDeletedFalse();
    int newHiresThisMonth = (int) employeeRepository.countByHireDateBetween(
        nowDate.withDayOfMonth(1),
        nowDate.withDayOfMonth(nowDate.lengthOfMonth())
    );
    int managerCount = (int) employeeRepository.countByRole(EmployeeRole.MANAGER);
    int employeeCount = (int) employeeRepository.countByRole(EmployeeRole.EMPLOYEE);

    DashboardDailyStats dailyStats = dashboardDailyStatsMapper.toEntity(
        nowDate,
        totalEmployees,
        newHiresThisMonth,
        managerCount,
        employeeCount
    );

    dailyStatsRepository.save(dailyStats);
  }

  @Override
  @Transactional
  public void departmentStats() {
    // 부서별 통계
    List<DashboardDepartmentStatsDto> statsDtos = departmentRepository.getDepartmentStats();

    List<DashboardDepartmentStats> stats = statsDtos.stream()
        .map(dto ->
            DashboardDepartmentStats.builder()
                .snapshotDate(dto.snapshotDate())
                .departmentName(dto.departmentName())
                .employeeCount(dto.employeeCount())
                .managerName(dto.managerName())
                .build()
        )
        .toList();

    departmentStatsRepository.saveAll(stats);
  }
}
