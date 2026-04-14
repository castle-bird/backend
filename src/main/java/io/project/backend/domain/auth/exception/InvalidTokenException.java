package io.project.backend.domain.auth.exception;

import io.project.backend.global.exception.ErrorCode;
import java.util.Map;

public class InvalidTokenException extends AuthException {
  public InvalidTokenException(Map<String, Object> details) {
    super(ErrorCode.TOKEN_INVALID, details);
  }
}
