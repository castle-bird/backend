package io.project.backend.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
  // Common
  INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "C001", "잘못된 입력값입니다."),
  METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "C002", "허용되지 않은 HTTP 메서드입니다."),
  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "C003", "서버 내부 오류가 발생했습니다."),

  // Auth
  INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "A001", "이메일 또는 비밀번호가 올바르지 않습니다."),
  TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "A002", "유효하지 않은 토큰입니다."),
  TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "A003", "만료된 토큰입니다."),
  TOKEN_REUSE_DETECTED(HttpStatus.UNAUTHORIZED, "A004", "토큰 재사용이 감지되었습니다."),

  // Employee
  EMPLOYEE_NOT_FOUND(HttpStatus.NOT_FOUND, "E001", "존재하지 않는 직원입니다."),
  EMPLOYEE_DUPLICATE(HttpStatus.CONFLICT, "E002", "이미 존재하는 직원입니다."),
  DEPARTMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "E003", "존재하지 않는 부서입니다.");

  private final HttpStatus status;
  private final String customCode;
  private final String description;

  ErrorCode(HttpStatus status, String customCode, String description) {
    this.status = status;
    this.customCode = customCode;
    this.description = description;
  }
}
