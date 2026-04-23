package io.project.backend.domain.notification.sse;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface SseService {

  SseEmitter subscribe(Long employeeId);
}
