package io.project.backend.domain.dashboard.repository;

import io.project.backend.domain.dashboard.entity.DashboardDailyStats;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DashboardDailyStatsRepository extends JpaRepository<DashboardDailyStats, Long> {

  Optional<DashboardDailyStats> findBySnapshotDate(LocalDate snapshotDate);
}
