package io.project.backend.domain.reservation.exception;

import io.project.backend.global.exception.ErrorCode;
import java.util.Map;

public class MeetingRoomDuplicateException extends MeetingRoomException {

  public MeetingRoomDuplicateException(Map<String, Object> details) {
    super(ErrorCode.MEETING_ROOM_NOT_ACTIVE, details);
  }
}
