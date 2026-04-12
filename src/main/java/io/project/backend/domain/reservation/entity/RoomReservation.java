package io.project.backend.domain.reservation.entity;

import io.project.backend.domain.employee.entity.Employee;
import io.project.backend.global.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnTransformer;

@Entity
@Table(name = "room_reservations")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoomReservation extends BaseTimeEntity {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "room_id", nullable = false)
  private MeetingRoom room;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "employee_id", nullable = false)
  private Employee employee;

  @Column(nullable = false, length = 255)
  private String purpose;

  @Column(name = "start_time", nullable = false)
  private Instant startTime;

  @Column(name = "end_time", nullable = false)
  private Instant endTime;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, columnDefinition = "reservation_status")
  @ColumnTransformer(read = "status::text", write = "?::reservation_status")
  private ReservationStatus status;

  // JPA 레벨 낙관적 잠금으로 동시 수정 충돌 감지
  @Version
  @Column(nullable = false)
  private int version;

  @Builder
  private RoomReservation(
      MeetingRoom room,
      Employee employee,
      String purpose,
      Instant startTime,
      Instant endTime
  ) {
    this.room = room;
    this.employee = employee;
    this.purpose = purpose;
    this.startTime = startTime;
    this.endTime = endTime;
    this.status = ReservationStatus.CONFIRMED;
  }

  public void cancel() {
    this.status = ReservationStatus.CANCELLED;
  }

  public void updateTime(Instant startTime, Instant endTime) {
    this.startTime = startTime;
    this.endTime = endTime;
  }
}
