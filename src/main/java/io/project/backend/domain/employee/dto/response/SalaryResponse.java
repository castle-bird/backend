package io.project.backend.domain.employee.dto.response;

import java.math.BigDecimal;

public record SalaryResponse(
    Long employeeId,
    BigDecimal yearSalary,
    BigDecimal monthSalary,
    Short paymentDay
) {
}
