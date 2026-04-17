package io.project.backend.domain.employee.exception;

import io.project.backend.global.exception.ErrorCode;
import java.util.Map;

public class SalaryNotFoundException extends EmployeeException {

  public SalaryNotFoundException(Map<String, Object> details) {
    super(ErrorCode.SALARY_NOT_FOUND, details);
  }
}
