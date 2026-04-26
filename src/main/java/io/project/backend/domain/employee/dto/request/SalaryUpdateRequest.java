package io.project.backend.domain.employee.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record SalaryUpdateRequest(
    @NotNull(message = "연봉은 필수입니다.")
    @Positive(message = "연봉은 양수여야 합니다.")
    BigDecimal yearSalary,

    @NotNull(message = "월급은 필수입니다.")
    @Positive(message = "월급은 양수여야 합니다.")
    BigDecimal monthSalary,

    @NotNull(message = "급여 지급일은 필수입니다.")
    @Min(value = 1, message = "급여 지급일은 1일 이상이어야 합니다.")
    @Max(value = 31, message = "급여 지급일은 31일 이하여야 합니다.")
    Short paymentDay
) {
}
