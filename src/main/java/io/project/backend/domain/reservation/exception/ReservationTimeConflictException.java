package io.project.backend.domain.reservation.exception;

import io.project.backend.global.exception.ErrorCode;
import java.util.Map;

public class ReservationTimeConflictException extends ReservationException {

  public ReservationTimeConflictException(Map<String, Object> details) {
    super(ErrorCode.RESERVATION_TIME_CONFLICT, details);
  }
}
