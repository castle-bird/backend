package io.project.backend.domain.reservation.controller.api;

import io.project.backend.domain.reservation.dto.request.ReservationCreateRequest;
import io.project.backend.domain.reservation.dto.response.ReservationResponse;
import io.project.backend.global.response.CommonApiResponse;
import io.project.backend.global.security.details.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.ResponseEntity;

@Tag(name = "Reservation", description = "회의실 예약 관련 API")
public interface ReservationControllerApi {

  @Operation(summary = "예약 생성", description = "회의실 예약을 생성합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "예약 성공"),
      @ApiResponse(responseCode = "400", description = "잘못된 요청 (시간 오류 등)", content = @Content),
      @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content),
      @ApiResponse(responseCode = "409", description = "예약 시간 충돌", content = @Content)
  })
  ResponseEntity<CommonApiResponse<ReservationResponse>> createReservation(
      UserDetailsImpl userDetails,
      ReservationCreateRequest request
  );

  @Operation(summary = "예약 취소", description = "예약자 본인 또는 관리자/매니저가 예약을 취소합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "취소 성공"),
      @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content),
      @ApiResponse(responseCode = "403", description = "권한 없음", content = @Content),
      @ApiResponse(responseCode = "404", description = "예약 없음", content = @Content)
  })
  ResponseEntity<CommonApiResponse<Void>> cancelReservation(
      UserDetailsImpl userDetails,
      Long reservationId
  );

  @Operation(summary = "나의 예약 조회", description = "본인의 예약 목록을 조회합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "조회 성공"),
      @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content)
  })
  ResponseEntity<CommonApiResponse<List<ReservationResponse>>> getReservationMe(
      UserDetailsImpl userDetails
  );

  @Operation(summary = "특정 회의실 예약 내역 조회", description = "특정 회의실의 예약 목록을 조회합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "조회 성공"),
      @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content),
      @ApiResponse(responseCode = "404", description = "회의실 없음", content = @Content)
  })
  ResponseEntity<CommonApiResponse<List<ReservationResponse>>> getReservations(
      Long roomId,
      LocalDate date
  );
}
