package io.project.backend.domain.reservation.repository;

import io.project.backend.domain.reservation.entity.RoomReservation;
import java.time.Instant;
import java.util.List;

public interface RoomReservationRepositoryCustom {

  List<RoomReservation> findByRoomAndDateRange(Long roomId, Instant startTime, Instant endTime);

  List<RoomReservation> findAllByEmployeeId(Long employeeId);

  List<RoomReservation> findAllByRoomId(Long roomId);
}
