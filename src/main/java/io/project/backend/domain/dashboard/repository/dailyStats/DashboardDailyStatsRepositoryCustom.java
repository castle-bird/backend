package io.project.backend.domain.dashboard.repository.dailyStats;

import io.project.backend.domain.dashboard.entity.DashboardDailyStats;
import java.time.LocalDate;
import java.util.Optional;

public interface DashboardDailyStatsRepositoryCustom  {

  Optional<DashboardDailyStats> findBySnapshotDate(LocalDate snapshotDate);
}
