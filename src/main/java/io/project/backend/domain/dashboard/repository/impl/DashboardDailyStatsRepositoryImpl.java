package io.project.backend.domain.dashboard.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import io.project.backend.domain.dashboard.entity.DashboardDailyStats;
import io.project.backend.domain.dashboard.entity.QDashboardDailyStats;
import io.project.backend.domain.dashboard.repository.dailyStats.DashboardDailyStatsRepositoryCustom;
import java.time.LocalDate;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DashboardDailyStatsRepositoryImpl implements DashboardDailyStatsRepositoryCustom {

  private final JPAQueryFactory queryFactory;
  private final QDashboardDailyStats dailyStats = QDashboardDailyStats.dashboardDailyStats;

  @Override
  public Optional<DashboardDailyStats> findBySnapshotDate(LocalDate snapshotDate) {
    return Optional.empty();
  }
}
