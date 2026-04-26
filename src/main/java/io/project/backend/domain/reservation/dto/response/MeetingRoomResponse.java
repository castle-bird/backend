package io.project.backend.domain.reservation.dto.response;

public record MeetingRoomResponse(
    Long id,
    String name,
    String location,
    boolean active
) {

}
