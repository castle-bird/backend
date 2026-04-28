package io.project.backend.domain.reservation.repository;

import io.project.backend.domain.employee.entity.Employee;
import io.project.backend.domain.reservation.entity.RoomReservation;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomReservationRepository extends JpaRepository<RoomReservation, Long>,
    RoomReservationRepositoryCustom {

  List<RoomReservation> findAllByEmployee(Employee employee);
}
