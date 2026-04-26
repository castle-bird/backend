package io.project.backend.domain.notification.dto.response;

import io.project.backend.domain.notification.entity.Notification;
import io.project.backend.domain.notification.entity.NotificationType;
import java.time.Instant;

public record NotificationResponse(
    Long notificationId,
    Long senderId,
    Long recipientId,
    NotificationType type,
    String title,
    String message,
    Instant createdAt
) {

  public static NotificationResponse from(Notification notification) {
    Long senderId = notification.getSender() == null ? null : notification.getSender().getId();
    Long recipientId = notification.getRecipient() == null ? null : notification.getRecipient().getId();

    return new NotificationResponse(
        notification.getId(),
        senderId,
        recipientId,
        notification.getType(),
        notification.getTitle(),
        notification.getMessage(),
        notification.getCreatedAt()
    );
  }
}
