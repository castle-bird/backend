package io.project.backend.domain.notification.controller.api;

import io.project.backend.domain.notification.dto.request.FullNotificationCreateRequest;
import io.project.backend.domain.notification.dto.response.NotificationResponse;
import io.project.backend.global.response.CommonApiResponse;
import io.project.backend.global.security.details.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;

@Tag(name = "Notification", description = "알림 API")
public interface NotificationControllerApi {

  @Operation(summary = "내 알림 조회", description = "본인 수신 알림과 전사 공지를 최신순으로 조회합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "조회 성공"),
      @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content)
  })
  ResponseEntity<CommonApiResponse<List<NotificationResponse>>> getMyNotifications(
      UserDetailsImpl userDetails
  );

  @Operation(summary = "전사 공지 생성", description = "관리자가 전사 공지 알림을 생성합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "생성 성공"),
      @ApiResponse(responseCode = "400", description = "요청 값 검증 실패", content = @Content),
      @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content),
      @ApiResponse(responseCode = "403", description = "권한 없음", content = @Content)
  })
  ResponseEntity<CommonApiResponse<Void>> createFullNotification(
      UserDetailsImpl userDetails,
      FullNotificationCreateRequest request
  );
}
