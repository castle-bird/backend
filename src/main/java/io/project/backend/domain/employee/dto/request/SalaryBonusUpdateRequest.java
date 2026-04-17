package io.project.backend.domain.employee.dto.request;

public record SalaryBonusUpdateRequest(
    Long employeeId,
    Long curBonus,
    Long newBonus
) {

}
