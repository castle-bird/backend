package io.project.backend.domain.reservation.service;

import io.project.backend.domain.reservation.dto.request.MeetingRoomCreateRequest;
import io.project.backend.domain.reservation.dto.request.MeetingRoomUpdateRequest;
import io.project.backend.domain.reservation.dto.response.MeetingRoomResponse;
import java.util.List;

public interface MeetingRoomService {

  void createMeetingRoom(MeetingRoomCreateRequest request);

  void updateMeetingRoom(Long roomId, MeetingRoomUpdateRequest request);

  MeetingRoomResponse getMeetingRoom(Long roomId);

  List<MeetingRoomResponse> getMeetingRoomList(Boolean active);
}
