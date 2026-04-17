package io.project.backend.domain.employee.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record SalaryUpdateRequest(
    @NotNull(message = "연봉은 필수입니다.")
    @Positive(message = "연봉은 양수여야 합니다.")
    BigDecimal yearSalary,

    @NotNull(message = "월급은 필수입니다.")
    @Positive(message = "월급은 양수여야 합니다.")
    BigDecimal monthSalary
) {
}
