package io.project.backend.domain.reservation.controller;

import io.project.backend.domain.reservation.controller.api.ReservationControllerApi;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reservations")
public class ReservationController implements ReservationControllerApi {

}
