package io.project.backend.domain.reservation.controller.api;

import io.project.backend.domain.reservation.dto.request.MeetingRoomCreateRequest;
import io.project.backend.domain.reservation.dto.request.MeetingRoomUpdateRequest;
import io.project.backend.domain.reservation.dto.response.MeetingRoomResponse;
import io.project.backend.global.response.CommonApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;

@Tag(name = "MeetingRoom", description = "회의실 관련 API")
public interface MeetingRoomControllerApi {

  @Operation(summary = "회의실 등록", description = "새로운 회의실을 등록합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "등록 성공"),
      @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content),
      @ApiResponse(responseCode = "403", description = "권한 없음", content = @Content)
  })
  ResponseEntity<CommonApiResponse<Void>> createMeetingRoom(
      MeetingRoomCreateRequest request
  );

  @Operation(summary = "회의실 수정", description = "회의실 정보를 수정합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "수정 성공"),
      @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content),
      @ApiResponse(responseCode = "403", description = "권한 없음", content = @Content),
      @ApiResponse(responseCode = "404", description = "회의실 없음", content = @Content)
  })
  ResponseEntity<CommonApiResponse<Void>> updateMeetingRoom(
      Long id,
      MeetingRoomUpdateRequest request
  );

  @Operation(summary = "회의실 단건 조회", description = "회의실 한 개의 정보를 조회합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "조회 성공"),
      @ApiResponse(responseCode = "404", description = "회의실 없음", content = @Content)
  })
  ResponseEntity<CommonApiResponse<MeetingRoomResponse>> getMeetingRoom(Long id);

  @Operation(summary = "회의실 목록 조회", description = "회의실 목록을 조회합니다. active 필터로 활성 상태를 필터링합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "조회 성공"),
      @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content)
  })
  ResponseEntity<CommonApiResponse<List<MeetingRoomResponse>>> getMeetingRoomList(Boolean active);
}
