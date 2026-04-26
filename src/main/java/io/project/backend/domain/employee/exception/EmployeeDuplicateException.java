package io.project.backend.domain.employee.exception;

import io.project.backend.global.exception.ErrorCode;
import java.util.Map;

public class EmployeeDuplicateException extends EmployeeException {

  public EmployeeDuplicateException(Map<String, Object> details) {
    super(ErrorCode.EMPLOYEE_DUPLICATE, details);
  }
}
