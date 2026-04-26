package io.project.backend.domain.reservation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Instant;

public record ReservationCreateRequest(
    @NotNull(message = "회의실 ID는 필수입니다.")
    Long roomId,

    @NotBlank(message = "회의 목적은 필수입니다.")
    @Size(min = 6, max=255, message = "회의 목적은 6자 이상 255자 이하로 입력해야 합니다.")
    String purpose,

    @NotNull(message = "회의 시작 시간은 필수입니다.")
    Instant startTime,

    @NotNull(message = "회의 종료 시간은 필수입니다.")
    Instant endTime
) {

}
