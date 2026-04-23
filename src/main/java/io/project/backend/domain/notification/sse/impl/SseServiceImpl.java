package io.project.backend.domain.notification.sse.impl;

import io.project.backend.domain.notification.sse.SseService;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@Slf4j
@RequiredArgsConstructor
public class SseServiceImpl implements SseService {

  private static final long SSE_TIMEOUT = 60 * 60 * 1000L; // 1시간
  private final Map<Long, Map<String, SseEmitter>> emitters = new ConcurrentHashMap<>();

  @Override
  public SseEmitter subscribe(Long employeeId) {
    String emitterId = employeeId + "_" + System.currentTimeMillis();
    SseEmitter emitter = new SseEmitter(SSE_TIMEOUT);

    // 여러 탭이나 디바이스에서 접속할 수 있으니, employeeId를 키로 여러 SseEmitter를 관리
    emitters.computeIfAbsent(employeeId, k -> new ConcurrentHashMap<>()).put(emitterId, emitter);

    // 연결 유지를 위해 최초 이벤트 전송
    try {
      emitter.send(SseEmitter.event()
          .name("FIRST_CONNECTION")
          .data("first connection established"));
      log.info("SSE 최초 접속 성공. employeeId: {}, emitterId: {}", employeeId, emitterId);
    } catch (Exception e) {
      log.error("서버와의 최초 접속이 실패했습니다. employeeId: {}, error: {}", employeeId, e.getMessage());
      removeEmitter(employeeId, emitterId);
      emitter.completeWithError(e);
    }

    // 연결이 끊겼을 때를 미리 처리 → SSE 기본 설정
    emitter.onCompletion(() -> {
      removeEmitter(employeeId, emitterId);
      log.info("SSE가 정상적으로 종료되었습니다. employeeId: {}, emitterId: {}", employeeId, emitterId);
    });

    emitter.onTimeout(() -> {
      removeEmitter(employeeId, emitterId);
      log.info("SSE가 시간이 초과되어 종료되었습니다. employeeId: {}, emitterId: {}", employeeId, emitterId);
    });

    emitter.onError((e) -> {
      removeEmitter(employeeId, emitterId);
      log.error("SSE 연결 중 오류가 발생했습니다. employeeId: {}, emitterId: {}, error: {}",
          employeeId, emitterId, e.getMessage());
    });

    return emitter;
  }

  @Override
  public void sendToEmployee(Long employeeId, String eventName, Object payload) {
    Map<String, SseEmitter> userEmitters = emitters.get(employeeId);

    if (userEmitters == null || userEmitters.isEmpty()) {
      return;
    }

    userEmitters.forEach(
        (emitterId, emitter) -> sendEvent(employeeId, emitterId, emitter, eventName, payload));
  }

  @Override
  public void sendToAll(String eventName, Object payload) {
    emitters.forEach((employeeId, userEmitters) -> {
      if (userEmitters == null || userEmitters.isEmpty()) {
        return;
      }
      userEmitters.forEach(
          (emitterId, emitter) -> sendEvent(employeeId, emitterId, emitter, eventName, payload));
    });
  }

  // 알림 보내기
  private void sendEvent(
      Long employeeId,
      String emitterId,
      SseEmitter emitter,
      String eventName,
      Object payload
  ) {
    try {
      emitter.send(
          SseEmitter.event()
              .name(eventName)
              .data(payload)
      );
    } catch (Exception e) {
      log.warn("SSE 이벤트 전송 실패. employeeId: {}, emitterId: {}, error: {}",
          employeeId, emitterId, e.getMessage());
      removeEmitter(employeeId, emitterId);
      emitter.completeWithError(e);
    }
  }

  // 연결 제거 → emitters에서 보관 중인 SseEmitter 제거하여 메모리 누수 방지
  private void removeEmitter(Long employeeId, String emitterId) {
    Map<String, SseEmitter> userEmitters = emitters.get(employeeId);

    if (userEmitters != null) {
      userEmitters.remove(emitterId);

      if (userEmitters.isEmpty()) {
        emitters.remove(employeeId);
      }
    }

    log.info("SSE 연결이 제거되었습니다. employeeId: {}, emitterId: {}", employeeId, emitterId);
  }
}
