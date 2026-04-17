package io.project.backend.domain.employee.dto.request;

public record SalaryUpdateRequest(
    Long employeeId,
    Long curAmount,
    Long newAmount
) {

}
