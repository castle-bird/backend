package io.project.backend.domain.reservation.repository;

import io.project.backend.domain.reservation.entity.RoomReservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomReservationRepository extends JpaRepository<RoomReservation, Long> {
}
