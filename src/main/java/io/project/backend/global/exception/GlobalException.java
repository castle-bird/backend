package io.project.backend.global.exception;

import java.util.Map;
import lombok.Getter;

@Getter
public class GlobalException extends RuntimeException {

  private final ErrorCode errorCode;
  private final Map<String, Object> details;

  public GlobalException(ErrorCode errorCode, Map<String, Object> details) {
    super(errorCode.getDescription());
    this.errorCode = errorCode;
    this.details = details;
  }
}
