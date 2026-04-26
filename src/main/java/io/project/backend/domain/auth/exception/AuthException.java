package io.project.backend.domain.auth.exception;

import io.project.backend.global.exception.ErrorCode;
import io.project.backend.global.exception.GlobalException;
import java.util.Map;

public class AuthException extends GlobalException {

  public AuthException(ErrorCode errorCode, Map<String, Object> details) {
    super(errorCode, details);
  }
}
