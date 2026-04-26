package io.project.backend.domain.dashboard.controller.api;

import io.project.backend.domain.dashboard.dto.response.DashboardDailyStatsResponse;
import io.project.backend.domain.dashboard.dto.response.DepartmentStatResponse;
import io.project.backend.global.response.CommonApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;

@Tag(name = "Dashboard", description = "대시보드 API")
public interface DashboardControllerApi {

    @Operation(summary = "직원·예약 현황 조회", description = "전체 직원 수, 역할별 인원, 당일·월간 예약 현황을 조회합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content),
        @ApiResponse(responseCode = "403", description = "권한 없음", content = @Content)
    })
    ResponseEntity<CommonApiResponse<List<DashboardDailyStatsResponse>>> getDailyStats();

    @Operation(summary = "부서별 현황 조회", description = "부서별 직원 수와 부서장 이름을 조회합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content),
        @ApiResponse(responseCode = "403", description = "권한 없음", content = @Content)
    })
    ResponseEntity<CommonApiResponse<List<DepartmentStatResponse>>> getDepartmentStats();

}
