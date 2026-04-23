package io.project.backend.domain.notification.pubsub;

import io.project.backend.domain.notification.sse.SseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class NotificationSseEventListener {

  private final SseService sseService;

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handle(NotificationSseEvent event) {
    if (event.broadcast()) {
      sseService.sendToAll(event.eventName(), event.payload());
      return;
    }

    if (event.employeeId() != null) {
      sseService.sendToEmployee(event.employeeId(), event.eventName(), event.payload());
    }
  }
}
