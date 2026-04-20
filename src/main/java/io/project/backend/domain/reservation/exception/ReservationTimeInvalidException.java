package io.project.backend.domain.reservation.exception;

import io.project.backend.global.exception.ErrorCode;
import java.util.Map;

public class ReservationTimeInvalidException extends ReservationException {

  public ReservationTimeInvalidException(Map<String, Object> details) {
    super(ErrorCode.RESERVATION_TIME_INVALID, details);
  }
}
