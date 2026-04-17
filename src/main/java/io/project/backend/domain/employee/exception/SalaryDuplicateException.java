package io.project.backend.domain.employee.exception;

import io.project.backend.global.exception.ErrorCode;
import java.util.Map;

public class SalaryDuplicateException extends EmployeeException {

  public SalaryDuplicateException(Map<String, Object> details) {
    super(ErrorCode.SALARY_DUPLICATE, details);
  }
}
