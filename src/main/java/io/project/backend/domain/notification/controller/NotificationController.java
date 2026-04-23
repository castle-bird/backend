package io.project.backend.domain.notification.controller;

import io.project.backend.domain.notification.controller.api.NotificationControllerApi;
import io.project.backend.domain.notification.dto.request.FullNotificationCreateRequest;
import io.project.backend.domain.notification.dto.response.NotificationResponse;
import io.project.backend.domain.notification.entity.NotificationType;
import io.project.backend.domain.notification.service.NotificationService;
import io.project.backend.domain.notification.sse.SseService;
import io.project.backend.global.response.CommonApiResponse;
import io.project.backend.global.security.details.UserDetailsImpl;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController implements NotificationControllerApi {

  private final NotificationService notificationService;
  private final SseService sseService;

  @Override
  @GetMapping(value = "/subscribe", produces = "text/event-stream")
  public SseEmitter subscribe(@AuthenticationPrincipal UserDetailsImpl userDetails) {
    return sseService.subscribe(userDetails.getUserId());
  }

  @Override
  @GetMapping("/me")
  public ResponseEntity<CommonApiResponse<List<NotificationResponse>>> getMyNotifications(
      @AuthenticationPrincipal UserDetailsImpl userDetails
  ) {
    List<NotificationResponse> response = notificationService.getMyNotifications(
        userDetails.getUserId());

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(CommonApiResponse.ok(response));
  }

  @Override
  @PostMapping("/full")
  public ResponseEntity<CommonApiResponse<Void>> createFullNotification(
      @AuthenticationPrincipal UserDetailsImpl userDetails,
      @Valid @RequestBody FullNotificationCreateRequest request
  ) {
    notificationService.notifyFullNotification(
        userDetails.getUserId(),
        NotificationType.FULL_NOTIFICATION,
        request.title(),
        request.message()
    );

    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(CommonApiResponse.created(null));
  }
}
