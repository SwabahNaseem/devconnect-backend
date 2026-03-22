package com.devconnect.repository;

import com.devconnect.model.Project;
import com.devconnect.model.ProjectFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProjectFileRepository extends JpaRepository<ProjectFile, Long> {

    // Get all files for a specific project
    List<ProjectFile> findByProject(Project project);

    // Get all files uploaded by a specific user in a project
    List<ProjectFile> findByProjectAndUploadedById(Project project, Long userId);
}