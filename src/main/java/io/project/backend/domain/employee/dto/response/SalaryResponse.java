package io.project.backend.domain.employee.dto.response;

public record SalaryResponse(
    Long employeeId,
    Long amount
) {

  public static SalaryResponse from(
      Long employeeId,
      Long amount
  ) {
    return new SalaryResponse(employeeId, amount);
  }
}
