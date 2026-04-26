package io.project.backend.domain.employee.exception.handler;

import io.project.backend.domain.employee.exception.EmployeeException;
import io.project.backend.global.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@RequiredArgsConstructor
public class EmployeeExceptionHandler {

  @ExceptionHandler(EmployeeException.class)
  public ResponseEntity<ErrorResponse> handleEmployeeException(
      EmployeeException e,
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
