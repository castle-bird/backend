package io.project.backend.domain.employee.exception;

import io.project.backend.global.exception.ErrorCode;
import io.project.backend.global.exception.GlobalException;
import java.util.Map;

public class DepartmentNotFoundException extends GlobalException {

  public DepartmentNotFoundException(Map<String, Object> details) {
    super(ErrorCode.DEPARTMENT_NOT_FOUND, details);
  }
}
