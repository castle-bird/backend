package io.project.backend.domain.reservation.repository;

import io.project.backend.domain.reservation.entity.MeetingRoom;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingRoomRepository extends JpaRepository<MeetingRoom, Long> {

  List<MeetingRoom> findAllByActiveTrue();
}
