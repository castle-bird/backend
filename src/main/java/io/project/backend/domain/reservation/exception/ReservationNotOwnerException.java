package io.project.backend.domain.reservation.exception;

import io.project.backend.global.exception.ErrorCode;
import java.util.Map;

public class ReservationNotOwnerException extends ReservationException {

  public ReservationNotOwnerException(Map<String, Object> details) {
    super(ErrorCode.RESERVATION_NOT_OWNED, details);
  }
}
