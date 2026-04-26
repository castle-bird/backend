package io.project.backend.domain.dashboard.batch;

public interface DashboardBatchService {

  void aggregateDailyStats();

  void aggregateDepartmentStats();
}
