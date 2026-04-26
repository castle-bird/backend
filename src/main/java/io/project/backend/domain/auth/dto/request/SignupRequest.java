package io.project.backend.domain.auth.dto.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import io.project.backend.domain.employee.entity.EmployeeRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SignupRequest(

    @NotBlank(message = "이름은 필수 입력값입니다.")
    @Size(min = 2, max = 50, message = "이름은 50자 이하여야 합니다.")
    String name,

    @NotBlank(message = "이메일은 필수 입력값입니다.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    @Size(max = 100, message = "이메일은 100자 이하여야 합니다.")
    String email,

    @NotNull(message = "권한은 필수 입력값입니다.")
    EmployeeRole role,

    @NotBlank(message = "직급은 필수 입력값입니다.")
    @Size(max = 50, message = "직급은 50자 이하여야 합니다.")
    @JsonAlias("position")
    String employeePosition,

    @NotBlank(message = "부서명은 필수 입력값입니다.")
    @Size(max = 100, message = "부서명은 100자 이하여야 합니다.")
    String department,

    @NotBlank(message = "주소는 필수 입력값입니다.")
    @Size(max = 255, message = "주소는 255자 이하여야 합니다.")
    String address,

    @NotBlank(message = "연락처는 필수 입력값입니다.")
    @Pattern(
        regexp = "^(01[016789]-\\d{3,4}-\\d{4}|02-\\d{3,4}-\\d{4}|0[3-9]\\d-\\d{3,4}-\\d{4})$",
        message = "올바른 전화번호 형식이 아닙니다. (예: 010-1234-5678)"
    )
    @Size(max = 13, message = "연락처는 13자 이하여야 합니다.")
    String phone
) {

}