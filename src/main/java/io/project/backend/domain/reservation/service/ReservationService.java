package io.project.backend.domain.reservation.service;

import io.project.backend.domain.reservation.dto.request.ReservationCreateRequest;
import io.project.backend.domain.reservation.dto.response.ReservationResponse;

public interface ReservationService {

  ReservationResponse createReservation(Long employeeId, ReservationCreateRequest request);
}
