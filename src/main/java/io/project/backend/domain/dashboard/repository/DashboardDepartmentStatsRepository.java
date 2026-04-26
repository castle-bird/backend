package io.project.backend.domain.dashboard.repository;

import io.project.backend.domain.dashboard.entity.DashboardDepartmentStats;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DashboardDepartmentStatsRepository extends JpaRepository<DashboardDepartmentStats, Long> {

  List<DashboardDepartmentStats> findAllBySnapshotDate(LocalDate snapshotDate);

  void deleteAllBySnapshotDate(LocalDate snapshotDate);
}
