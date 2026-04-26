package io.project.backend.domain.dashboard.dto.response;

import io.project.backend.domain.dashboard.entity.DashboardDailyStats;
import java.time.LocalDate;

public record DashboardDailyStatsResponse(
    LocalDate snapshotDate,

    // 직원 현황
    int totalEmployees,
    int newHiresThisMonth,
    int managerCount,
    int employeeCount,

    // 예약 현황
    int todayConfirmed,
    int todayCancelled,
    int monthlyTotalReservations
) {

  public static DashboardDailyStatsResponse from(DashboardDailyStats entity) {
    return new DashboardDailyStatsResponse(
        entity.getSnapshotDate(),
        entity.getTotalEmployees(),
        entity.getNewHiresThisMonth(),
        entity.getManagerCount(),
        entity.getEmployeeCount(),
        entity.getTodayConfirmed(),
        entity.getTodayCancelled(),
        entity.getMonthlyTotalReservations()
    );
  }
}
