package com.devconnect.repository;

import com.devconnect.model.Message;
import com.devconnect.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    // Get all messages for a project, ordered by time (oldest first)
    List<Message> findByProjectOrderBySentAtAsc(Project project);
}