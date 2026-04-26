package io.project.backend.domain.dashboard.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import io.project.backend.domain.dashboard.entity.DashboardDepartmentStats;
import io.project.backend.domain.dashboard.entity.QDashboardDepartmentStats;
import io.project.backend.domain.dashboard.repository.departmentStats.DashboardDepartmentStatsRepositoryCustom;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DashboardDepartmentStatsRepositoryImpl implements
    DashboardDepartmentStatsRepositoryCustom {

  private final JPAQueryFactory queryFactory;
  private final QDashboardDepartmentStats departmentStats = QDashboardDepartmentStats.dashboardDepartmentStats;


  @Override
  public List<DashboardDepartmentStats> findAllBySnapshotDate(LocalDate snapshotDate) {
    return List.of();
  }

  @Override
  public void deleteAllBySnapshotDate(LocalDate snapshotDate) {

  }
}
