package io.project.backend.domain.dashboard.entity;

import io.project.backend.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "dashboard_daily_stats")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DashboardDailyStats extends BaseEntity {

  @Column(nullable = false, unique = true)
  private LocalDate snapshotDate;

  // 직원 현황
  @Column(nullable = false)
  private int totalEmployees;

  @Column(nullable = false)
  private int newHiresThisMonth;

  @Column(nullable = false)
  private int managerCount;

  @Column(nullable = false)
  private int employeeCount;

  // 예약 현황
  @Column(nullable = false)
  private int todayConfirmed;

  @Column(nullable = false)
  private int todayCancelled;

  @Column(nullable = false)
  private int monthlyTotalReservations;

  @Builder
  private DashboardDailyStats(
      LocalDate snapshotDate,
      int totalEmployees,
      int newHiresThisMonth,
      int managerCount,
      int employeeCount,
      int todayConfirmed,
      int todayCancelled,
      int monthlyTotalReservations
  ) {
    this.snapshotDate = snapshotDate;
    this.totalEmployees = totalEmployees;
    this.newHiresThisMonth = newHiresThisMonth;
    this.managerCount = managerCount;
    this.employeeCount = employeeCount;
    this.todayConfirmed = todayConfirmed;
    this.todayCancelled = todayCancelled;
    this.monthlyTotalReservations = monthlyTotalReservations;
  }

  public void update(
      int totalEmployees,
      int newHiresThisMonth,
      int managerCount,
      int employeeCount,
      int todayConfirmed,
      int todayCancelled,
      int monthlyTotalReservations
  ) {
    this.totalEmployees = totalEmployees;
    this.newHiresThisMonth = newHiresThisMonth;
    this.managerCount = managerCount;
    this.employeeCount = employeeCount;
    this.todayConfirmed = todayConfirmed;
    this.todayCancelled = todayCancelled;
    this.monthlyTotalReservations = monthlyTotalReservations;
  }
}
