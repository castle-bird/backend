package io.project.backend.domain.notification.repository;

import io.project.backend.domain.notification.entity.Notification;
import java.util.List;

public interface NotificationRepositoryCustom {

  List<Notification> findVisibleByRecipientId(Long recipientId);
}
