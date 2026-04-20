package io.project.backend.domain.reservation.exception;

import io.project.backend.global.exception.ErrorCode;
import java.util.Map;

public class ReservationNotFoundException extends ReservationException {

  public ReservationNotFoundException(Map<String, Object> details) {
    super(ErrorCode.RESERVATION_NOT_FOUND, details);
  }
}
