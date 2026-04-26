package io.project.backend.domain.notification.mapper;

import io.project.backend.domain.employee.entity.Employee;
import io.project.backend.domain.notification.entity.Notification;
import io.project.backend.domain.notification.entity.NotificationType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

  @Mapping(target = "sender", source = "sender")
  @Mapping(target = "recipient", source = "recipient")
  @Mapping(target = "type", source = "type")
  @Mapping(target = "title", source = "title")
  @Mapping(target = "message", source = "message")
  Notification toEntityForCreate(
      Employee sender,
      Employee recipient,
      NotificationType type,
      String title,
      String message
  );
}
