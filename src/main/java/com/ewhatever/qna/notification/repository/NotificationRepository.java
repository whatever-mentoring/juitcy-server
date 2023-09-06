package com.ewhatever.qna.notification.repository;

import com.ewhatever.qna.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
