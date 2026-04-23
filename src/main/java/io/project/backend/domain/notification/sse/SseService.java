package io.project.backend.domain.notification.sse;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface SseService {

  SseEmitter subscribe(Long employeeId);

  void sendToEmployee(Long employeeId, String eventName, Object payload);

  void sendToAll(String eventName, Object payload);
}
