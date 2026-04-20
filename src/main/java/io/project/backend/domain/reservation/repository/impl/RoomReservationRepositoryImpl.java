package io.project.backend.domain.reservation.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import io.project.backend.domain.employee.entity.QEmployee;
import io.project.backend.domain.reservation.entity.QMeetingRoom;
import io.project.backend.domain.reservation.entity.QRoomReservation;
import io.project.backend.domain.reservation.entity.ReservationStatus;
import io.project.backend.domain.reservation.entity.RoomReservation;
import io.project.backend.domain.reservation.repository.RoomReservationRepositoryCustom;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RoomReservationRepositoryImpl implements RoomReservationRepositoryCustom {

  private final JPAQueryFactory queryFactory;
  private final QRoomReservation reservation = QRoomReservation.roomReservation;
  private final QEmployee employee = QEmployee.employee;
  private final QMeetingRoom meetingRoom = QMeetingRoom.meetingRoom;

  @Override
  public List<RoomReservation> findByRoomAndDateRange(
      Long roomId,
      Instant startTime,
      Instant endTime
  ) {
    return queryFactory
        .selectFrom(reservation)
        .where(
            reservation.room.id.eq(roomId),
            reservation.endTime.gt(startTime)
                .and(reservation.startTime.lt(endTime)),
            reservation.status.eq(ReservationStatus.CONFIRMED)
        )
        .orderBy(reservation.startTime.asc())
        .fetch();
  }

  @Override
  public List<RoomReservation> findAllByEmployeeId(Long employeeId) {
    return queryFactory
        .selectFrom(reservation)
        .join(reservation.employee, employee).fetchJoin()
        .join(reservation.room, meetingRoom).fetchJoin()
        .where(employee.id.eq(employeeId))
        .orderBy(reservation.startTime.desc())
        .fetch();
  }
}
