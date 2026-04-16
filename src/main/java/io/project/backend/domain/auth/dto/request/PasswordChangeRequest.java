package io.project.backend.domain.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record PasswordChangeRequest(
    @NotBlank(message = "현재 비밀번호는 필수 입력값입니다.")
    String currentPwd,

    @NotBlank(message = "변경 비밀번호는 필수 입력값입니다.")
    @Size(min = 8, max = 255, message = "비밀번호는 8자 이상 255자 이하여야 합니다.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]+$",
        message = "비밀번호는 영문자, 숫자, 특수문자를 모두 포함해야 합니다.")
    String newPwd
) {
}
