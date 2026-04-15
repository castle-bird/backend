package io.project.backend.global.response;

import org.springframework.http.HttpStatus;

public record CommonApiResponse<T>(
    int status,     // HTTP 상태 코드
    String message, // 응답 메시지
    T data          // 실제 데이터 (없으면 null)
) {

  /** 200 OK */
  public static <T> CommonApiResponse<T> ok(T data) {
    return new CommonApiResponse<>(HttpStatus.OK.value(), "success", data);
  }

  /** 201 Created */
  public static <T> CommonApiResponse<T> created(T data) {
    return new CommonApiResponse<>(HttpStatus.CREATED.value(), "created", data);
  }
}
