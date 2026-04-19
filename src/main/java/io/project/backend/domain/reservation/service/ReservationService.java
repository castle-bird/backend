package io.project.backend.domain.reservation.service;

import io.project.backend.domain.reservation.dto.request.ReservationCreateRequest;
import io.project.backend.domain.reservation.dto.response.ReservationResponse;
import java.util.List;

public interface ReservationService {

  ReservationResponse createReservation(Long employeeId, ReservationCreateRequest request);

  void cancelReservation(Long employeeId, Long reservationId);

  List<ReservationResponse> getReservationMe(Long employeeId);

  List<ReservationResponse> getReservations(Long roomId);
}
