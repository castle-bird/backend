package io.project.backend.domain.auth.exception;

import io.project.backend.global.exception.ErrorCode;
import java.util.Map;

public class ExpiredTokenException extends AuthException {
  public ExpiredTokenException(Map<String, Object> details) {
    super(ErrorCode.TOKEN_EXPIRED, details);
  }
}
