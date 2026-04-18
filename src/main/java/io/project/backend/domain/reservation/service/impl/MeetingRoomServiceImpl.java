package io.project.backend.domain.reservation.service.impl;

import io.project.backend.domain.reservation.dto.request.MeetingRoomCreateRequest;
import io.project.backend.domain.reservation.dto.request.MeetingRoomUpdateRequest;
import io.project.backend.domain.reservation.dto.response.MeetingRoomResponse;
import io.project.backend.domain.reservation.service.MeetingRoomService;
import io.project.backend.global.security.details.UserDetailsImpl;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MeetingRoomServiceImpl implements MeetingRoomService {

  @Override
  public void createMeetingRoom(UserDetailsImpl userDetails, MeetingRoomCreateRequest request) {

  }

  @Override
  public void updateMeetingRoom(UserDetailsImpl userDetails, Long roomId,
      MeetingRoomUpdateRequest request) {

  }

  @Override
  public MeetingRoomResponse getMeetingRoom(Long roomId) {
    return null;
  }

  @Override
  public List<MeetingRoomResponse> getMeetingRoomList(Boolean active) {
    return List.of();
  }
}
