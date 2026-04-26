package io.project.backend.domain.dashboard.repository;

import io.project.backend.domain.dashboard.entity.DashboardDepartmentStats;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DashboardDepartmentStatsRepository extends
    JpaRepository<DashboardDepartmentStats, Long>,
    DashboardDepartmentStatsRepositoryCustom {

}
