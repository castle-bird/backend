package io.project.backend.global.config;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.admin")
public record AdminProperties(

    @NotBlank(message = "관리자 이메일은 필수입니다.")
    @Email(message = "관리자 이메일 형식이 올바르지 않습니다.")
    String email,

    @NotBlank(message = "관리자 비밀번호는 필수입니다.")
    String password,

    @NotBlank(message = "관리자 이름은 필수입니다.")
    String name
) {

}
