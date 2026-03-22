package com.devconnect.repository;

import com.devconnect.model.Notification;
import com.devconnect.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // All notifications for a user, newest first
    List<Notification> findByRecipientOrderByCreatedAtDesc(User recipient);

    // Only unread notifications for a user
    List<Notification> findByRecipientAndIsReadFalseOrderByCreatedAtDesc(User recipient);

    // Count unread
    long countByRecipientAndIsReadFalse(User recipient);

    // Mark all as read for a user
    @Modifying
    @Transactional
    @Query("UPDATE Notification n SET n.isRead = true WHERE n.recipient = :user")
    void markAllReadForUser(User user);
}