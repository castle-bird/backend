package io.project.backend.domain.reservation.controller.api;

import io.project.backend.domain.reservation.dto.request.ReservationCreateRequest;
import io.project.backend.domain.reservation.dto.response.ReservationResponse;
import io.project.backend.global.response.CommonApiResponse;
import io.project.backend.global.security.details.UserDetailsImpl;
import org.springframework.http.ResponseEntity;

public interface ReservationControllerApi {

  ResponseEntity<CommonApiResponse<ReservationResponse>> createReservation(
      UserDetailsImpl userDetails,
      ReservationCreateRequest request
  );
}
