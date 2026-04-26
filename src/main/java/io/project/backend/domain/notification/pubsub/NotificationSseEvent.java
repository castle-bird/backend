package io.project.backend.domain.notification.pubsub;

import io.project.backend.domain.notification.dto.response.NotificationResponse;

public record NotificationSseEvent(
    Long employeeId,
    boolean broadcast,
    String eventName,
    NotificationResponse payload
) {

  public static NotificationSseEvent toEmployee(
      Long employeeId,
      String eventName,
      NotificationResponse payload
  ) {
    return new NotificationSseEvent(employeeId, false, eventName, payload);
  }

  public static NotificationSseEvent toAll(
      String eventName,
      NotificationResponse payload
  ) {
    return new NotificationSseEvent(null, true, eventName, payload);
  }
}
