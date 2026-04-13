package io.project.backend.domain.auth.exception;

import io.project.backend.global.exception.ErrorCode;
import java.util.Map;

public class AuthenticationException extends AuthException {

  public AuthenticationException(Map<String, Object> details) {
    super(ErrorCode.INVALID_CREDENTIALS, details);
  }
}
