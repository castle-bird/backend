package io.project.backend.domain.reservation.mapper;

import io.project.backend.domain.reservation.dto.request.MeetingRoomCreateRequest;
import io.project.backend.domain.reservation.entity.MeetingRoom;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MeetingRoomMapper {

  @Mapping(target = "name", source = "request.name")
  @Mapping(target = "location", source = "request.location")
  MeetingRoom toEntityForCreate(MeetingRoomCreateRequest request);
}
