package io.project.backend.global.exception.handler;


import io.project.backend.global.exception.ErrorCode;
import io.project.backend.global.exception.GlobalException;
import io.project.backend.global.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  /** Global Exception - 비즈니스 로직에서 발생하는 예외 처리 */
  @ExceptionHandler(GlobalException.class)
  public ResponseEntity<ErrorResponse> handleGlobalException(
      GlobalException e,
      HttpServletRequest request
  ) {
    log.error("[GlobalException] - Message: {}", e.getMessage(), e);

    ErrorResponse errorResponse = ErrorResponse.of(
        e.getErrorCode(),
        request.getRequestURI(),
        e.getDetails()
    );

    return ResponseEntity
        .status(e.getErrorCode().getStatus())
        .body(errorResponse);
  }

  /** <code>@Valid</code> 유효성 검사 실패 */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(
      MethodArgumentNotValidException e,
      HttpServletRequest request
  ) {
    log.error("[ValidationException] - Message: {}", e.getMessage());

    // 필드별 에러 메시지 수집
    Map<String, Object> details = e.getBindingResult()
        .getFieldErrors()
        .stream()
        .collect(Collectors.toMap(
            FieldError::getField,
            fe -> fe.getDefaultMessage() != null ? fe.getDefaultMessage() : "유효하지 않은 값입니다.",
            (prev, curr) -> prev  // 동일 필드 중복 시 이전 값 유지
        ));

    ErrorResponse errorResponse = ErrorResponse.of(
        ErrorCode.INVALID_INPUT_VALUE,
        request.getRequestURI(),
        details
    );

    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(errorResponse);
  }

  /** JSON 파싱/바인딩 실패 (예: enum 값 불일치) */
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(
      HttpMessageNotReadableException e,
      HttpServletRequest request
  ) {
    log.error("[HttpMessageNotReadableException] - Message: {}", e.getMessage());

    ErrorResponse errorResponse = ErrorResponse.of(
        ErrorCode.INVALID_INPUT_VALUE,
        request.getRequestURI(),
        Map.of("request", "요청 본문 형식 또는 값이 올바르지 않습니다.")
    );

    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(errorResponse);
  }

  /** @PreAuthorize 등 메서드 권한 체크 실패 */
  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ErrorResponse> handleAccessDeniedException(
      AccessDeniedException e,
      HttpServletRequest request
  ) {
    log.warn("[AccessDeniedException] - URI: {}, Message: {}", request.getRequestURI(), e.getMessage());

    ErrorResponse errorResponse = ErrorResponse.of(
        ErrorCode.ACCESS_DENIED,
        request.getRequestURI(),
        Map.of("request", "해당 리소스에 접근할 권한이 없습니다.")
    );

    return ResponseEntity
        .status(HttpStatus.FORBIDDEN)
        .body(errorResponse);
  }

  /** 존재하지 않는 정적 리소스 요청 */
  @ExceptionHandler(NoResourceFoundException.class)
  public ResponseEntity<ErrorResponse> handleNoResourceFoundException(
      NoResourceFoundException e,
      HttpServletRequest request
  ) {
    log.warn("[NoResourceFoundException] - URI: {}", request.getRequestURI());

    ErrorResponse errorResponse = ErrorResponse.of(
        ErrorCode.INTERNAL_SERVER_ERROR,
        request.getRequestURI(),
        Map.of("request", "요청한 리소스를 찾을 수 없습니다.")
    );

    return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(errorResponse);
  }

  /** 미처리 Exception */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGlobalException(
      Exception e,
      HttpServletRequest request
  ) {
    log.error("[Exception] - Message: {}", e.getMessage(), e);

    ErrorResponse errorResponse = ErrorResponse.of(
        ErrorCode.INTERNAL_SERVER_ERROR,
        request.getRequestURI(),
        Map.of("request", "예상치 못한 오류가 발생했습니다. 관리자에게 문의하세요.")
    );

    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(errorResponse);
  }
}
