package io.project.backend.domain.employee.exception;

import io.project.backend.global.exception.ErrorCode;
import io.project.backend.global.exception.GlobalException;
import java.util.Map;

public class EmployeeException extends GlobalException {

  public EmployeeException(ErrorCode errorCode, Map<String, Object> details) {
    super(errorCode, details);
  }
}
