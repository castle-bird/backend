package io.project.backend.domain.reservation.exception;

import io.project.backend.global.exception.ErrorCode;
import java.util.Map;

public class ReservationAlreadyCancelException extends ReservationException {

  public ReservationAlreadyCancelException(Map<String, Object> details) {
    super(ErrorCode.RESERVATION_ALREADY_CANCELLED, details);
  }
}
