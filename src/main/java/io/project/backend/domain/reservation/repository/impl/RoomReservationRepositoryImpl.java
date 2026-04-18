package io.project.backend.domain.reservation.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
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
}
