package io.project.backend.domain.reservation.service.impl;

import io.project.backend.domain.reservation.dto.request.MeetingRoomCreateRequest;
import io.project.backend.domain.reservation.dto.request.MeetingRoomUpdateRequest;
import io.project.backend.domain.reservation.dto.response.MeetingRoomResponse;
import io.project.backend.domain.reservation.entity.MeetingRoom;
import io.project.backend.domain.reservation.exception.MeetingRoomDuplicateException;
import io.project.backend.domain.reservation.exception.MeetingRoomNotFoundException;
import io.project.backend.domain.reservation.mapper.MeetingRoomMapper;
import io.project.backend.domain.reservation.repository.MeetingRoomRepository;
import io.project.backend.domain.reservation.service.MeetingRoomService;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MeetingRoomServiceImpl implements MeetingRoomService {

  private final MeetingRoomRepository meetingRoomRepository;
  private final MeetingRoomMapper meetingRoomMapper;

  @Override
  @Transactional
  public void createMeetingRoom(MeetingRoomCreateRequest request) {

    // 회의실 이름 중복 체크
    if (meetingRoomRepository.existsByName(request.name())) {
      throw new MeetingRoomDuplicateException(
          Map.of("name", request.name())
      );
    }

    meetingRoomRepository.save(meetingRoomMapper.toEntityForCreate(request));
  }

  @Override
  @Transactional
  public void updateMeetingRoom(
      Long roomId,
      MeetingRoomUpdateRequest request
  ) {
    // 회의실 존재 여부 체크
    MeetingRoom meetingRoom = meetingRoomRepository.findById(roomId)
        .orElseThrow(() -> new MeetingRoomNotFoundException(Map.of("roomId", roomId)));

    // 회의실 이름 중복 체크 (자기 자신 제외)
    if (meetingRoomRepository.existsByNameAndIdNot(request.name(), roomId)) {
      throw new MeetingRoomDuplicateException(
          Map.of("name", request.name())
      );
    }

    meetingRoom.updateInfo(request.name(), request.location(), request.active());
  }

  @Override
  public MeetingRoomResponse getMeetingRoom(Long roomId) {

    MeetingRoom meetingRoom = meetingRoomRepository.findById(roomId)
        .orElseThrow(() -> new MeetingRoomNotFoundException(Map.of("roomId", roomId)));

    return meetingRoomMapper.toResponseForEntity(meetingRoom);
  }

  @Override
  public List<MeetingRoomResponse> getMeetingRoomList(Boolean active) {

    List<MeetingRoom> meetingRooms = meetingRoomRepository.findAllByActiveIs(active);

    return meetingRooms.stream()
        .map(meetingRoomMapper::toResponseForEntity)
        .toList();
  }
}
