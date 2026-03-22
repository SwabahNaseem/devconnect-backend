// ─── JoinRequestRepository.java ───────────────────────────────
package com.devconnect.repository;

import com.devconnect.model.JoinRequest;
import com.devconnect.model.Project;
import com.devconnect.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface JoinRequestRepository extends JpaRepository<JoinRequest, Long> {

    // Get all requests for a specific project
    List<JoinRequest> findByProject(Project project);

    // Get all pending requests for a project
    List<JoinRequest> findByProjectAndStatus(Project project, JoinRequest.RequestStatus status);

    // Check if a user already sent a request to a project
    Optional<JoinRequest> findByUserAndProject(User user, Project project);

    // Check if request exists at all
    boolean existsByUserAndProject(User user, Project project);
}