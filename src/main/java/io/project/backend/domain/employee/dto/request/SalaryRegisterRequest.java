package io.project.backend.domain.employee.dto.request;

public record SalaryRegisterRequest(
    Long employeeId,
    Long amount
) {
}
