package io.project.backend.domain.reservation.repository;

import io.project.backend.domain.reservation.entity.MeetingRoom;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingRoomRepository extends JpaRepository<MeetingRoom, Long> {

  List<MeetingRoom> findAllByActiveIs(boolean active);

  Optional<MeetingRoom> findByName(String name);

  boolean existsByNameAndIdNot(String name, Long id);

  boolean existsByName(String name);
}
