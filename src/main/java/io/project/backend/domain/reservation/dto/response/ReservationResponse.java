package io.project.backend.domain.reservation.dto.response;

import java.time.Instant;

public record ReservationResponse(
    Long reservationId,
    Long roomId,
    String roomName,
    String roomLocation,
    String employeeName,
    String purpose,
    Instant startTime,
    Instant endTime,
    String status
) {

}
