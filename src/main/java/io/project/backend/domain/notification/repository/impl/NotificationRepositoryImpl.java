package io.project.backend.domain.notification.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import io.project.backend.domain.employee.entity.QEmployee;
import io.project.backend.domain.notification.entity.Notification;
import io.project.backend.domain.notification.entity.QNotification;
import io.project.backend.domain.notification.repository.NotificationRepositoryCustom;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class NotificationRepositoryImpl implements NotificationRepositoryCustom {

  private final JPAQueryFactory queryFactory;
  private final QNotification notification = QNotification.notification;
  private final QEmployee sender = new QEmployee("sender");
  private final QEmployee recipient = new QEmployee("recipient");

  @Override
  public List<Notification> findVisibleByRecipientId(Long recipientId) {
    return queryFactory
        .selectFrom(notification)
        .join(notification.sender, sender).fetchJoin()
        .leftJoin(notification.recipient, recipient).fetchJoin()
        .where(
            notification.recipient.id.eq(recipientId)
                .or(notification.recipient.isNull())
        )
        .orderBy(notification.createdAt.desc())
        .fetch();
  }
}
