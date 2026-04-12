package io.project.backend.domain.reservation.repository;

import io.project.backend.domain.reservation.entity.MeetingRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingRoomRepository extends JpaRepository<MeetingRoom, Long> {
}
