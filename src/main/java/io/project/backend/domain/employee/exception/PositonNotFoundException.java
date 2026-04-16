package io.project.backend.domain.employee.exception;

import io.project.backend.global.exception.ErrorCode;
import java.util.Map;

public class PositonNotFoundException extends EmployeeException {

  public PositonNotFoundException(Map<String, Object> details) {
    super(ErrorCode.EMPLOYEE_NOT_FOUND, details);
  }
}
