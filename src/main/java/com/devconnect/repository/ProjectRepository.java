package com.devconnect.repository;

import com.devconnect.model.Project;
import com.devconnect.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    // Find all projects created by a specific user (lead)
    List<Project> findByLead(User lead);

    // Find all projects a user is a member of
    List<Project> findByMembersContaining(User user);

    // Search projects by name (case-insensitive)
    // %name% means "contains" the search term
    List<Project> findByNameContainingIgnoreCase(String name);

    // Search by name OR description
    @Query("SELECT p FROM Project p WHERE " +
           "LOWER(p.name) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
           "LOWER(p.description) LIKE LOWER(CONCAT('%', :term, '%'))")
    List<Project> searchProjects(String term);
}