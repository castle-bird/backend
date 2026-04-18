package io.project.backend.domain.reservation.exception;

import io.project.backend.global.exception.ErrorCode;
import java.util.Map;

public class MeetingRoomNotFoundException extends MeetingRoomException {

  public MeetingRoomNotFoundException(Map<String, Object> details) {
    super(ErrorCode.MEETING_ROOM_NOT_FOUND, details);
  }
}
