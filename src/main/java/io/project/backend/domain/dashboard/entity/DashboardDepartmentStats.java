package io.project.backend.domain.dashboard.entity;

import io.project.backend.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
    name = "dashboard_department_stats",
    uniqueConstraints = @UniqueConstraint(
        name = "uq_dept_stats_date_dept",
        columnNames = {"snapshot_date", "department_name"}
    )
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DashboardDepartmentStats extends BaseEntity {

  @Column(nullable = false)
  private LocalDate snapshotDate;

  @Column(nullable = false, length = 100)
  private String departmentName;

  @Column(nullable = false)
  private int employeeCount;

  @Column(length = 50)
  private String managerName; // 부서장 미지정이면 NULL

  @Builder
  private DashboardDepartmentStats(
      LocalDate snapshotDate,
      String departmentName,
      int employeeCount,
      String managerName
  ) {
    this.snapshotDate = snapshotDate;
    this.departmentName = departmentName;
    this.employeeCount = employeeCount;
    this.managerName = managerName;
  }
}
