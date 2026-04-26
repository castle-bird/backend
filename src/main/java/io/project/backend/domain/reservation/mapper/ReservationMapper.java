package io.project.backend.domain.reservation.mapper;

import io.project.backend.domain.employee.entity.Employee;
import io.project.backend.domain.reservation.dto.request.ReservationCreateRequest;
import io.project.backend.domain.reservation.dto.response.ReservationResponse;
import io.project.backend.domain.reservation.entity.MeetingRoom;
import io.project.backend.domain.reservation.entity.RoomReservation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReservationMapper {

  @Mapping(target = "employee", source = "employee")
  @Mapping(target = "room", source = "meetingRoom")
  RoomReservation toEntityForCreate(Employee employee, MeetingRoom meetingRoom, ReservationCreateRequest request);

  @Mapping(target = "reservationId", source = "roomReservation.id")
  @Mapping(target = "roomId", source = "roomReservation.room.id")
  @Mapping(target = "employeeName", source = "roomReservation.employee.name")
  @Mapping(target = "roomName", source = "roomReservation.room.name")
  @Mapping(target = "roomLocation", source = "roomReservation.room.location")
  ReservationResponse toResponseForEntity(RoomReservation roomReservation);
}
