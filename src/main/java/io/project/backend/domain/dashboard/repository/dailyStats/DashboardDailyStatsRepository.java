package io.project.backend.domain.dashboard.repository.dailyStats;

import io.project.backend.domain.dashboard.entity.DashboardDailyStats;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DashboardDailyStatsRepository extends JpaRepository<DashboardDailyStats, Long> {

}
