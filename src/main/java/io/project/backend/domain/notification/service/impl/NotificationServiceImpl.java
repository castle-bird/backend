package io.project.backend.domain.notification.service.impl;

import io.project.backend.domain.employee.entity.Employee;
import io.project.backend.domain.employee.exception.EmployeeNotFoundException;
import io.project.backend.domain.employee.repository.EmployeeRepository;
import io.project.backend.domain.notification.dto.response.NotificationResponse;
import io.project.backend.domain.notification.entity.NotificationType;
import io.project.backend.domain.notification.mapper.NotificationMapper;
import io.project.backend.domain.notification.repository.NotificationRepository;
import io.project.backend.domain.notification.service.NotificationService;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

  private final NotificationRepository notificationRepository;
  private final EmployeeRepository employeeRepository;
  private final NotificationMapper notificationMapper;

  @Override
  @Transactional
  public void notifySalaryChanged(
      Long senderId,
      Long recipientId,
      NotificationType type,
      String title,
      String message
  ) {
    Employee sender = getEmployeeById(senderId);
    Employee recipient = getEmployeeById(recipientId);

    notificationRepository.save(
        notificationMapper.toEntityForCreate(
            sender,
            recipient,
            type,
            title,
            message
        )
    );
  }

  @Override
  @Transactional
  public void notifyEmployeeChanged(
      Long senderId,
      Long recipientId,
      NotificationType type,
      String title,
      String message
  ) {
    Employee sender = getEmployeeById(senderId);
    Employee recipient = getEmployeeById(recipientId);

    notificationRepository.save(
        notificationMapper.toEntityForCreate(
            sender,
            recipient,
            type,
            title,
            message
        )
    );
  }

  @Override
  @Transactional
  public void notifyReservationCancelled(
      Long senderId,
      Long recipientId,
      NotificationType type,
      String title,
      String message
  ) {
    Employee sender = getEmployeeById(senderId);
    Employee recipient = getEmployeeById(recipientId);

    notificationRepository.save(
        notificationMapper.toEntityForCreate(
            sender,
            recipient,
            type,
            title,
            message
        )
    );
  }

  @Override
  @Transactional
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public void notifyFullNotification(
      Long senderId,
      NotificationType type,
      String title,
      String message
  ) {
    Employee sender = getEmployeeById(senderId);

    notificationRepository.save(
        notificationMapper.toEntityForCreate(
            sender,
            null,
            type,
            title,
            message
        )
    );
  }

  @Override
  @Transactional(readOnly = true)
  public List<NotificationResponse> getMyNotifications(Long recipientId) {
    return notificationRepository.findVisibleByRecipientId(recipientId)
        .stream()
        .map(NotificationResponse::from)
        .toList();
  }

  private Employee getEmployeeById(Long employeeId) {
    return employeeRepository.findByIdAndDeletedFalse(employeeId)
        .orElseThrow(() -> new EmployeeNotFoundException(Map.of("employeeId", employeeId)));
  }
}
