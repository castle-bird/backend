package io.project.backend.domain.notification.service;

import io.project.backend.domain.notification.entity.NotificationType;

public interface NotificationService {

  void notifySalaryChanged(
      Long senderId,
      Long recipientId,
      NotificationType type,
      String title,
      String message
  );

  void notifyEmployeeChanged(
      Long senderId,
      Long recipientId,
      NotificationType type,
      String title,
      String message
  );

  void notifyReservationCancelled(
      Long senderId,
      Long recipientId,
      NotificationType type,
      String title,
      String message
  );

  void notifyFullNotification(
      Long senderId,
      NotificationType type,
      String title,
      String message
  );
}

