package io.project.backend.domain.reservation.exception;

import io.project.backend.domain.employee.exception.EmployeeException;
import io.project.backend.global.exception.ErrorCode;
import java.util.Map;

public class MeetingRoomNotFoundException extends EmployeeException {

  public MeetingRoomNotFoundException(Map<String, Object> details) {
    super(ErrorCode.MEETING_ROOM_NOT_FOUND, details);
  }
}
