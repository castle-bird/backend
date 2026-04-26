package io.project.backend.domain.dashboard.scheduler;

import io.project.backend.domain.dashboard.batch.DashboardBatchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DashboardScheduler {

  private final DashboardBatchService dashboardBatchService;

  @Scheduled(cron = "0 0 23 * * *") // 매일 23시
  public void scheduleDashboardAggregation() {
    dashboardBatchService.dailyStats();
    dashboardBatchService.departmentStats();
  }
}
