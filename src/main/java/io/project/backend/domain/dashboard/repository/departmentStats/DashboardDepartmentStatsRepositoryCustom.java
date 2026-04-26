package io.project.backend.domain.dashboard.repository.departmentStats;

import io.project.backend.domain.dashboard.entity.DashboardDepartmentStats;
import java.time.LocalDate;
import java.util.List;

public interface DashboardDepartmentStatsRepositoryCustom {

  List<DashboardDepartmentStats> findAllBySnapshotDate(LocalDate snapshotDate);

  void deleteAllBySnapshotDate(LocalDate snapshotDate);
}
