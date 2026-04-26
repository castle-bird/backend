package io.project.backend.domain.reservation.exception.hanlder;

import io.project.backend.domain.reservation.exception.ReservationException;
import io.project.backend.global.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@RequiredArgsConstructor
public class ReservationExceptionHandler {

  @ExceptionHandler(ReservationException.class)
  public ResponseEntity<ErrorResponse> handleReservationException(
      ReservationException e,
      HttpServletRequest request
  ) {
    return ResponseEntity
        .status(e.getErrorCode().getStatus())
        .body(ErrorResponse.of(
            e.getErrorCode(),
            request.getRequestURI(),
            e.getDetails()
        ));
  }
}
