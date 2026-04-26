package io.project.backend.domain.reservation.exception;

import io.project.backend.global.exception.ErrorCode;
import io.project.backend.global.exception.GlobalException;
import java.util.Map;

public class ReservationException extends GlobalException {

  public ReservationException(ErrorCode errorCode, Map<String, Object> details) {
    super(errorCode, details);
  }
}
