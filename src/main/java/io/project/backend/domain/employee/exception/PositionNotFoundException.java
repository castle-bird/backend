package io.project.backend.domain.employee.exception;

import io.project.backend.global.exception.ErrorCode;
import java.util.Map;

public class PositionNotFoundException extends EmployeeException {

  public PositionNotFoundException(Map<String, Object> details) {
    super(ErrorCode.POSITION_INVALID, details);
  }
}
