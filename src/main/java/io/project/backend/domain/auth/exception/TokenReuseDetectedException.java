package io.project.backend.domain.auth.exception;

import io.project.backend.global.exception.ErrorCode;
import java.util.Map;

public class TokenReuseDetectedException extends AuthException {
  public TokenReuseDetectedException(Map<String, Object> details) {
    super(ErrorCode.TOKEN_REUSE_DETECTED, details);
  }
}
