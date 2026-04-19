package io.project.backend.domain.reservation.controller;

import io.project.backend.domain.reservation.controller.api.ReservationControllerApi;
import io.project.backend.domain.reservation.dto.request.ReservationCreateRequest;
import io.project.backend.domain.reservation.dto.response.ReservationResponse;
import io.project.backend.domain.reservation.service.ReservationService;
import io.project.backend.global.response.CommonApiResponse;
import io.project.backend.global.security.details.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reservations")
public class ReservationController implements ReservationControllerApi {

  private final ReservationService reservationService;

  @Override
  @PostMapping
  public ResponseEntity<CommonApiResponse<ReservationResponse>> createReservation(
      @AuthenticationPrincipal UserDetailsImpl userDetails,
      @Valid @RequestBody ReservationCreateRequest request
  ) {

    ReservationResponse response = reservationService.createReservation(
        userDetails.getUserId(),
        request
    );

    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(CommonApiResponse.created(response));
  }
}
