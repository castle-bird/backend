package io.project.backend.global.response;

import io.project.backend.global.exception.ErrorCode;
import java.time.LocalDateTime;
import java.util.Map;

public record ErrorResponse(
    LocalDateTime timestamp,    // 발생 시각
    int status,                 // HTTP 상태 코드
    String customCode,          // 커스텀 에러 코드
    String description,         // 에러 메시지
    String path,                // 요청 경로
    Map<String, Object> details      // 에러 상세내역
) {

  /**
   * <p>상세정보 없는 에러 응답</p>
   *
   * @param errorCode 에러 코드
   * @param path 경로
   * @return 에러 응답 객체
   */
  public static ErrorResponse of(
      ErrorCode errorCode,
      String path
  ) {
    return new ErrorResponse(
        LocalDateTime.now(),
        errorCode.getStatus().value(),
        errorCode.getCustomCode(),
        errorCode.getDescription(),
        path,
        Map.of()
    );
  }

  /**
   * <p>상세정보 있는 에러 응답</p>
   *
   * @param errorCode 에러 코드
   * @param path 경로
   * @param details 에러 상세
   * @return 에러 응답 객체
   */
  public static ErrorResponse of(
      ErrorCode errorCode,
      String path,
      Map<String, Object> details
  ) {
    return new ErrorResponse(
        LocalDateTime.now(),
        errorCode.getStatus().value(),
        errorCode.getCustomCode(),
        errorCode.getDescription(),
        path,
        details
    );
  }
}
