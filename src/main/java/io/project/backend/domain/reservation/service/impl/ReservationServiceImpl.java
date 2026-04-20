package io.project.backend.domain.reservation.service.impl;

import io.project.backend.domain.employee.entity.Employee;
import io.project.backend.domain.employee.entity.EmployeeRole;
import io.project.backend.domain.employee.exception.EmployeeNotFoundException;
import io.project.backend.domain.employee.repository.EmployeeRepository;
import io.project.backend.domain.notification.entity.NotificationType;
import io.project.backend.domain.notification.service.NotificationService;
import io.project.backend.domain.reservation.dto.request.ReservationCreateRequest;
import io.project.backend.domain.reservation.dto.response.ReservationResponse;
import io.project.backend.domain.reservation.entity.MeetingRoom;
import io.project.backend.domain.reservation.entity.ReservationStatus;
import io.project.backend.domain.reservation.entity.RoomReservation;
import io.project.backend.domain.reservation.exception.MeetingRoomNotActiveException;
import io.project.backend.domain.reservation.exception.MeetingRoomNotFoundException;
import io.project.backend.domain.reservation.exception.ReservationAlreadyCancelException;
import io.project.backend.domain.reservation.exception.ReservationNotFoundException;
import io.project.backend.domain.reservation.exception.ReservationNotOwnerException;
import io.project.backend.domain.reservation.exception.ReservationTimeConflictException;
import io.project.backend.domain.reservation.exception.ReservationTimeInvalidException;
import io.project.backend.domain.reservation.mapper.ReservationMapper;
import io.project.backend.domain.reservation.repository.MeetingRoomRepository;
import io.project.backend.domain.reservation.repository.RoomReservationRepository;
import io.project.backend.domain.reservation.service.ReservationService;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationServiceImpl implements ReservationService {

  private static final LocalTime WORK_START_HOUR = LocalTime.of(9, 0);
  private static final LocalTime WORK_END_HOUR = LocalTime.of(18, 0);
  private static final ZoneId KOREA_ZONE = ZoneId.of("Asia/Seoul");

  private final RoomReservationRepository roomReservationRepository;
  private final EmployeeRepository employeeRepository;
  private final MeetingRoomRepository meetingRoomRepository;
  private final ReservationMapper reservationMapper;
  private final NotificationService notificationService;

  @Override
  @Transactional
  public ReservationResponse createReservation(
      Long employeeId,
      ReservationCreateRequest request
  ) {
    // 회의실 존재 여부 검증 및 활성화 여부
    MeetingRoom meetingRoom = meetingRoomRepository.findById(request.roomId())
        .orElseThrow(() -> new MeetingRoomNotFoundException(Map.of("roomId", request.roomId())));

    if (!meetingRoom.isActive()) {
      throw new MeetingRoomNotActiveException(Map.of("roomId", request.roomId()));
    }

    // 시간 유효성 검증: 예약 시작 시간이 종료 시간보다 이전인지 확인
    if (!request.startTime().isBefore(request.endTime())) {
      throw new ReservationTimeInvalidException(Map.of(
          "message", "예약 시작 시간은 종료 시간보다 이전이어야 합니다."
      ));
    }

    // 시간 간격 검증 (09:00 ~ 18:00): 회사 정책에 따라 예약 가능한 시간대를 제한
    LocalTime startTime = request.startTime().atZone(KOREA_ZONE).toLocalTime();
    LocalTime endTime = request.endTime().atZone(KOREA_ZONE).toLocalTime();

    if (startTime.isBefore(WORK_START_HOUR) || endTime.isAfter(WORK_END_HOUR)) {
      throw new ReservationTimeInvalidException(Map.of(
          "message", "예약 시간은 09:00 ~ 18:00 사이여야 합니다."
      ));
    }

    // 시간 겹치는게 있는지 검증
    List<RoomReservation> roomReservations = roomReservationRepository.findByRoomAndDateRange(
        request.roomId(),
        request.startTime(),
        request.endTime()
    );

    if (!roomReservations.isEmpty()) {
      throw new ReservationTimeConflictException(Map.of(
          "roomId", request.roomId(),
          "startTime", request.startTime(),
          "endTime", request.endTime()
      ));
    }

    // 예약 생성
    Employee employee = employeeRepository.findById(employeeId)
        .orElseThrow(() -> new EmployeeNotFoundException(Map.of("employeeId", employeeId)));

    RoomReservation reservation = roomReservationRepository.save(
        reservationMapper.toEntityForCreate(employee, meetingRoom, request)
    );

    return reservationMapper.toResponseForEntity(reservation);
  }

  @Override
  @Transactional
  public void cancelReservation(Long employeeId, Long reservationId) {
    Employee actor = employeeRepository.findByIdAndDeletedFalse(employeeId)
        .orElseThrow(() -> new EmployeeNotFoundException(Map.of("employeeId", employeeId)));

    // 예약 존재 여부 검증
    RoomReservation reservation = roomReservationRepository.findById(reservationId)
        .orElseThrow(() -> new ReservationNotFoundException(Map.of(
            "reservationId", reservationId
        )));

    // 예약 소유자 또는 관리자/매니저만 취소 가능
    boolean isOwner = reservation.getEmployee().getId().equals(employeeId);
    boolean isManagerOrAdmin =
        actor.getRole() == EmployeeRole.ADMIN || actor.getRole() == EmployeeRole.MANAGER;

    if (!isOwner && !isManagerOrAdmin) {
      throw new ReservationNotOwnerException(Map.of("reservationId", reservationId));
    }

    // 이미 취소된 예약인지 검증
    if (reservation.getStatus() == ReservationStatus.CANCELLED) {
      throw new ReservationAlreadyCancelException(Map.of(
          "reservationId", reservationId
      ));
    }

    // 예약 취소 (삭제)
    reservation.cancel();

    notificationService.notifyReservationCancelled(
        employeeId,
        reservation.getEmployee().getId(),
        NotificationType.RESERVATION_CANCELLED,
        "회의실 예약 취소 알림",
        "예약하신 회의실 예약이 취소되었습니다. 예약 내역을 확인해주세요."
    );
  }

  @Override
  @Transactional(readOnly = true)
  public List<ReservationResponse> getReservationMe(Long employeeId) {

    List<RoomReservation> reservations = roomReservationRepository.findAllByEmployeeId(employeeId);

    return reservations.stream().map(reservationMapper::toResponseForEntity).toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<ReservationResponse> getReservations(Long roomId, LocalDate date) {

    Instant startOfDay = date.atStartOfDay(KOREA_ZONE).toInstant();
    Instant endOfDay = date.atTime(LocalTime.MAX).atZone(KOREA_ZONE).toInstant();

    List<RoomReservation> reservations = roomReservationRepository.findByRoomAndDateRange(
        roomId, startOfDay, endOfDay
    );

    return reservations.stream().map(reservationMapper::toResponseForEntity).toList();
  }
}
