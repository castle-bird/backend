package io.project.backend.domain.notification.entity;

import io.project.backend.domain.employee.entity.Employee;
import io.project.backend.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnTransformer;

@Entity
@Table(name = "notifications")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends BaseEntity {

  // 발신자가 삭제(soft delete)되어도 알림 이력은 보존 → cascade 명시 안함
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "sender_id")
  private Employee sender;

  // SALARY_CHANGED, POSITION_CHANGED, RESERVATION_CANCELLED 등 개인 알림 시 필수
  // FULL_NOTIFICATION은 NULL (전체 발송)
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "recipient_id")
  private Employee recipient;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, columnDefinition = "notification_type")
  @ColumnTransformer(read = "type::text", write = "?::notification_type")
  private NotificationType type;

  @Column(nullable = false, length = 200)
  private String title;

  @Column(columnDefinition = "TEXT")
  private String message;

  @Builder
  private Notification(
      Employee sender,
      Employee recipient,
      NotificationType type,
      String title,
      String message
  ) {
    this.sender = sender;
    this.recipient = recipient;
    this.type = type;
    this.title = title;
    this.message = message;
  }
}
