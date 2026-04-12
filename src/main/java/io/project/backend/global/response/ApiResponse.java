package io.project.backend.global.response;

import org.springframework.http.HttpStatus;

public record ApiResponse<T>(
    int status,     // HTTP 상태 코드
    String message, // 응답 메시지
    T data          // 실제 데이터 (없으면 null)
) {

  /** 200 OK */
  public static <T> ApiResponse<T> ok(T data) {
    return new ApiResponse<>(HttpStatus.OK.value(), "success", data);
  }

  /** 201 Created */
  public static <T> ApiResponse<T> created(T data) {
    return new ApiResponse<>(HttpStatus.CREATED.value(), "created", data);
  }
}
