package io.project.backend.domain.notification.dto.request;

import jakarta.validation.constraints.NotBlank;

public record FullNotificationCreateRequest(
    @NotBlank(message = "title은 필수입니다.")
    String title,
    String message
) {
}
