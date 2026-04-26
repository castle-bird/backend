package io.project.backend.domain.dashboard.batch.impl;

import io.project.backend.domain.dashboard.batch.DashboardBatchService;
import io.project.backend.domain.dashboard.dto.common.DashboardDepartmentStatsDto;
import io.project.backend.domain.dashboard.entity.DashboardDailyStats;
import io.project.backend.domain.dashboard.entity.DashboardDepartmentStats;
import io.project.backend.domain.dashboard.mapper.DashboardDailyStatsMapper;
import io.project.backend.domain.dashboard.repository.DashboardDailyStatsRepository;
import io.project.backend.domain.dashboard.repository.DashboardDepartmentStatsRepository;
import io.project.backend.domain.employee.entity.EmployeeRole;
import io.project.backend.domain.employee.repository.DepartmentRepository;
import io.project.backend.domain.employee.repository.EmployeeRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
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

  // 대시보드
  private final DashboardDailyStatsRepository dailyStatsRepository;
  private final DashboardDepartmentStatsRepository departmentStatsRepository;

  @Override
  @Transactional
  public void dailyStats() {
    LocalDate nowDate = LocalDate.now();

    // 회원 정보 추출
    int totalEmployees = (int) employeeRepository.countByDeletedFalse();
    int newHiresThisMonth = (int) employeeRepository.countByHireDateBetween(
        nowDate.withDayOfMonth(1),
        nowDate.withDayOfMonth(nowDate.lengthOfMonth())
    );
    int managerCount = (int) employeeRepository.countByRole(EmployeeRole.MANAGER);
    int employeeCount = (int) employeeRepository.countByRole(EmployeeRole.EMPLOYEE);

    // 기존 데이터 업데이트 또는 새 데이터 저장 → 혹시 모를 중복 방어
    Optional<DashboardDailyStats> existing = dailyStatsRepository.findBySnapshotDate(nowDate);

    if (existing.isPresent()) {
      existing.get().update(totalEmployees, newHiresThisMonth, managerCount, employeeCount);
    } else {
      dailyStatsRepository.save(
          dashboardDailyStatsMapper.toEntity(
              nowDate,
              totalEmployees,
              newHiresThisMonth,
              managerCount,
              employeeCount
          )
      );
    }
  }

  @Override
  @Transactional
  public void departmentStats() {
    LocalDate nowDate = LocalDate.now();

    // 기존 데이터 삭제 → 혹시 모를 중복 방어
    departmentStatsRepository.deleteAllBySnapshotDate(nowDate);

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
