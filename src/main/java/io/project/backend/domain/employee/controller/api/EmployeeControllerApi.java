package io.project.backend.domain.employee.controller.api;

import io.project.backend.domain.employee.dto.request.UpdateEmployeeRequest;
import io.project.backend.domain.employee.dto.response.DepartmentListResponse;
import io.project.backend.domain.employee.dto.response.EmployeeListResponse;
import io.project.backend.domain.employee.dto.response.EmployeeResponse;
import io.project.backend.domain.employee.dto.response.PositionListResponse;
import io.project.backend.global.response.CommonApiResponse;
import io.project.backend.global.security.details.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;

@Tag(name = "Employee", description = "직원 관련 API")
public interface EmployeeControllerApi {

  @Operation(summary = "부서 목록 전체 조회", description = "부서 목록을 전체 조회합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "조회 성공"),
      @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content)
  })
  ResponseEntity<CommonApiResponse<List<DepartmentListResponse>>> getDepartmentList();

  @Operation(summary = "직급 목록 전체 조회", description = "직급 목록을 전체 조회합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "조회 성공"),
      @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content)
  })
  ResponseEntity<CommonApiResponse<List<PositionListResponse>>> getPositionList();

  @Operation(summary = "직원 삭제", description = "직원을 Soft Delete로 삭제합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "삭제 성공"),
      @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content),
      @ApiResponse(responseCode = "403", description = "권한 없음", content = @Content),
      @ApiResponse(responseCode = "404", description = "직원 없음", content = @Content)
  })
  ResponseEntity<CommonApiResponse<Void>> deleteEmployee(Long id);

  @Operation(summary = "직원 업데이트", description = "직원의 정보를 업데이트합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "수정 성공"),
      @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content),
      @ApiResponse(responseCode = "403", description = "권한 없음", content = @Content),
      @ApiResponse(responseCode = "404", description = "직원 없음", content = @Content)
  })
  ResponseEntity<CommonApiResponse<Void>> updateEmployee(Long id, UpdateEmployeeRequest request);

  @Operation(summary = "직원 단건 조회", description = "직원 한 명의 정보를 조회합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "조회 성공"),
      @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content),
      @ApiResponse(responseCode = "403", description = "권한 없음", content = @Content),
      @ApiResponse(responseCode = "404", description = "직원 없음", content = @Content)
  })
  ResponseEntity<CommonApiResponse<EmployeeResponse>> getEmployee(Long id);

  @Operation(summary = "전체 직원 조회", description = "전체 직원 정보를 조회합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "조회 성공"),
      @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content),
      @ApiResponse(responseCode = "403", description = "권한 없음", content = @Content),
      @ApiResponse(responseCode = "404", description = "직원 없음", content = @Content)
  })
  ResponseEntity<CommonApiResponse<EmployeeListResponse>> getEmployeeList(
      String department,
      String position,
      Long cursor,
      int size
  );

  @Operation(summary = "본인의 정보를 조회", description = "본인의 정보를 조회합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "조회 성공"),
      @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content),
      @ApiResponse(responseCode = "403", description = "권한 없음", content = @Content),
      @ApiResponse(responseCode = "404", description = "직원 없음", content = @Content)
  })
  ResponseEntity<CommonApiResponse<EmployeeResponse>> getMe(
      UserDetailsImpl userDetails
  );
}
