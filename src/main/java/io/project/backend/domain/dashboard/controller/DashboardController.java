package io.project.backend.domain.dashboard.controller;

import io.project.backend.domain.dashboard.controller.api.DashboardControllerApi;
import io.project.backend.domain.dashboard.dto.response.DashboardDailyStatsResponse;
import io.project.backend.domain.dashboard.dto.response.DepartmentStatResponse;
import io.project.backend.domain.dashboard.service.DashboardService;
import io.project.backend.global.response.CommonApiResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController implements DashboardControllerApi {

  private final DashboardService dashboardService;

  @Override
  @GetMapping("/stats")
  public ResponseEntity<CommonApiResponse<DashboardDailyStatsResponse>> getDailyStats() {

    DashboardDailyStatsResponse response = dashboardService.getDailyStats();

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(CommonApiResponse.ok(response));
  }

  @Override
  @GetMapping("/departments")
  public ResponseEntity<CommonApiResponse<List<DepartmentStatResponse>>> getDepartmentStats() {

    List<DepartmentStatResponse> response = dashboardService.getDepartmentStats();

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(CommonApiResponse.ok(response));
  }
}
