package io.project.backend.domain.dashboard.repository;

import io.project.backend.domain.dashboard.entity.DashboardDepartmentStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DashboardDepartmentStatsRepository extends JpaRepository<DashboardDepartmentStats, Long> {

}
