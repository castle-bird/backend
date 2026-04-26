package io.project.backend.domain.notification.entity;

public enum NotificationType {
  // 예약 취소 시 예약자에게 발송 (관리자/매니저가 타인 예약을 취소한 경우)
  RESERVATION_CANCELLED,

  // 전사 공지 (특정 수신자 없이 전체 발송)
  FULL_NOTIFICATION,

  // 개인 대상 알림 - 해당 직원에게만 발송 (recipient_id 필수)
  SALARY_CHANGED,
  EMPLOYEE_CHANGED
}
