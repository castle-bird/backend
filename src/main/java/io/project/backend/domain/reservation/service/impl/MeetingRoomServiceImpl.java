package io.project.backend.domain.reservation.service.impl;

import io.project.backend.domain.reservation.dto.request.MeetingRoomCreateRequest;
import io.project.backend.domain.reservation.dto.request.MeetingRoomUpdateRequest;
import io.project.backend.domain.reservation.dto.response.MeetingRoomResponse;
import io.project.backend.domain.reservation.exception.MeetingRoomDuplicateException;
import io.project.backend.domain.reservation.mapper.MeetingRoomMapper;
import io.project.backend.domain.reservation.repository.MeetingRoomRepository;
import io.project.backend.domain.reservation.service.MeetingRoomService;
import io.project.backend.global.security.details.UserDetailsImpl;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MeetingRoomServiceImpl implements MeetingRoomService {

  private final MeetingRoomRepository meetingRoomRepository;
  private final MeetingRoomMapper meetingRoomMapper;

  @Override
  public void createMeetingRoom(UserDetailsImpl userDetails, MeetingRoomCreateRequest request) {

    // 회의실 이름 중복 체크
    if (meetingRoomRepository.existsByName(request.name())) {
      throw new MeetingRoomDuplicateException(
          Map.of("name", request.name())
      );
    }

    meetingRoomRepository.save(meetingRoomMapper.toEntityForCreate(request));
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
