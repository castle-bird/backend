package io.project.backend.domain.employee.controller.api;

import io.project.backend.domain.employee.dto.request.SalaryRegisterRequest;
import io.project.backend.domain.employee.dto.request.SalaryUpdateRequest;
import io.project.backend.domain.employee.dto.response.SalaryResponse;
import io.project.backend.global.response.CommonApiResponse;
import io.project.backend.global.security.details.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

@Tag(name = "Salary", description = "급여 관리 API")
public interface SalaryControllerApi {

  @Operation(summary = "급여 정보 등록", description = "직원의 연봉/월급/지급일 정보를 등록합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "등록 성공"),
      @ApiResponse(responseCode = "400", description = "요청 값 검증 실패", content = @Content),
      @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content),
      @ApiResponse(responseCode = "403", description = "권한 없음", content = @Content),
      @ApiResponse(responseCode = "404", description = "직원 없음", content = @Content),
      @ApiResponse(responseCode = "409", description = "이미 급여 정보가 등록된 직원", content = @Content)
  })
  ResponseEntity<CommonApiResponse<Void>> registerSalary(SalaryRegisterRequest request);

  @Operation(summary = "직원 급여 조회", description = "직원 ID로 급여 정보를 조회합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "조회 성공"),
      @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content),
      @ApiResponse(responseCode = "403", description = "권한 없음", content = @Content),
      @ApiResponse(responseCode = "404", description = "직원 또는 급여 정보 없음", content = @Content)
  })
  ResponseEntity<CommonApiResponse<SalaryResponse>> getSalary(Long employeeId);

  @Operation(summary = "내 급여 조회", description = "인증된 사용자의 급여 정보를 조회합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "조회 성공"),
      @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content),
      @ApiResponse(responseCode = "404", description = "직원 또는 급여 정보 없음", content = @Content)
  })
  ResponseEntity<CommonApiResponse<SalaryResponse>> getSalaryMe(UserDetails userDetails);

  @Operation(summary = "급여 정보 수정", description = "직원 ID 기준으로 연봉/월급/급여 지급일을 수정합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "수정 성공"),
      @ApiResponse(responseCode = "400", description = "요청 값 검증 실패", content = @Content),
      @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content),
      @ApiResponse(responseCode = "403", description = "권한 없음", content = @Content),
      @ApiResponse(responseCode = "404", description = "직원 또는 급여 정보 없음", content = @Content)
  })
  ResponseEntity<CommonApiResponse<Void>> updateSalary(
      UserDetailsImpl userDetails,
      Long employeeId,
      SalaryUpdateRequest request
  );
}
