package io.project.backend.domain.reservation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MeetingRoomCreateRequest(
    @NotBlank(message = "회의실 이름은 필수 입력값입니다.")
    @Size(max = 50, message = "회의실 이름은 50자 이하여야 합니다.")
    String name,

    @NotBlank(message = "위치는 필수 입력값입니다.")
    @Size(max = 100, message = "위치는 100자 이하여야 합니다.")
    String location
) {

}
