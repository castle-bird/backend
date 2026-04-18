package io.project.backend.domain.reservation.service;

import io.project.backend.domain.reservation.dto.request.MeetingRoomCreateRequest;
import io.project.backend.domain.reservation.dto.request.MeetingRoomUpdateRequest;
import io.project.backend.domain.reservation.dto.response.MeetingRoomResponse;
import io.project.backend.global.security.details.UserDetailsImpl;
import java.util.List;

public interface MeetingRoomService {

  void createMeetingRoom(UserDetailsImpl userDetails, MeetingRoomCreateRequest request);

  void updateMeetingRoom(UserDetailsImpl userDetails, Long roomId,
      MeetingRoomUpdateRequest request);

  MeetingRoomResponse getMeetingRoom(Long roomId);

  List<MeetingRoomResponse> getMeetingRoomList(Boolean active);
}
