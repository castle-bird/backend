package io.project.backend.domain.reservation.controller;

import io.project.backend.domain.reservation.controller.api.MeetingRoomControllerApi;
import io.project.backend.domain.reservation.dto.request.MeetingRoomCreateRequest;
import io.project.backend.domain.reservation.dto.request.MeetingRoomUpdateRequest;
import io.project.backend.domain.reservation.dto.response.MeetingRoomResponse;
import io.project.backend.domain.reservation.service.MeetingRoomService;
import io.project.backend.global.response.CommonApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rooms")
public class MeetingRoomController implements MeetingRoomControllerApi {

  private final MeetingRoomService meetingRoomService;

  @Override
  @PostMapping
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
  public ResponseEntity<CommonApiResponse<Void>> createMeetingRoom(
      @Valid @RequestBody MeetingRoomCreateRequest request
  ) {

    meetingRoomService.createMeetingRoom(request);

    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(CommonApiResponse.created(null));
  }

  @Override
  @PutMapping("/{id}")
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
  public ResponseEntity<CommonApiResponse<Void>> updateMeetingRoom(
      @PathVariable Long id,
      @Valid @RequestBody MeetingRoomUpdateRequest request
  ) {

    meetingRoomService.updateMeetingRoom(id, request);

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(CommonApiResponse.created(null));
  }

  @Override
  @GetMapping("/{id}")
  public ResponseEntity<CommonApiResponse<MeetingRoomResponse>> getMeetingRoom(
      @PathVariable Long id) {
    return null;
  }

  @Override
  @GetMapping()
  public ResponseEntity<CommonApiResponse<List<MeetingRoomResponse>>> getMeetingRoomList(
      @RequestParam(defaultValue = "true") Boolean active
  ) {
    return null;
  }
}
