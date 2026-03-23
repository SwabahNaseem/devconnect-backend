package com.devconnect.service;

import com.devconnect.dto.*;
import com.devconnect.model.Project.ProjectStatus;
import com.devconnect.model.*;
import com.devconnect.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final JoinRequestRepository joinRequestRepository;
    private final ProjectFileRepository projectFileRepository;
    private final FileStorageService fileStorageService;
    private final NotificationService notificationService;

    private static final String BASE_URL = "http://localhost:8080/";

    public ProjectService(ProjectRepository projectRepository,
                          UserRepository userRepository,
                          JoinRequestRepository joinRequestRepository,
                          ProjectFileRepository projectFileRepository,
                          FileStorageService fileStorageService,
                          NotificationService notificationService) {
        this.projectRepository    = projectRepository;
        this.userRepository       = userRepository;
        this.joinRequestRepository= joinRequestRepository;
        this.projectFileRepository= projectFileRepository;
        this.fileStorageService   = fileStorageService;
        this.notificationService  = notificationService;
    }

    // ─────────────────────── PROJECTS ────────────────────────

    public ProjectResponse createProject(String email, ProjectRequest request) {
        User lead = getUserByEmail(email);
        List<String> skills = request.getSkills() != null ? request.getSkills() : new ArrayList<>();
        int maxM = (request.getMaxMembers() != null && request.getMaxMembers() > 0) ? request.getMaxMembers() : 4;
        Project project = Project.builder()
                .name(request.getName()).description(request.getDescription())
                .skills(skills).lead(lead).maxMembers(maxM).build();
        project.getMembers().add(lead);
        return mapToProjectResponse(projectRepository.save(project), lead);
    }

    public List<ProjectResponse> getAllProjects(String email) {
        User currentUser = getUserByEmail(email);
        List<ProjectResponse> result = new ArrayList<>();
        for (Project p : projectRepository.findAll()) {
            if (p.getStatus() != ProjectStatus.DELETED) result.add(mapToProjectResponse(p, currentUser));
        }
        return result;
    }

    public ProjectResponse getProject(Long projectId, String email) {
        return mapToProjectResponse(getProjectById(projectId), getUserByEmail(email));
    }

    public List<ProjectResponse> getMyProjects(String email) {
        User user = getUserByEmail(email);
        List<ProjectResponse> result = new ArrayList<>();
        for (Project p : projectRepository.findByMembersContaining(user)) result.add(mapToProjectResponse(p, user));
        return result;
    }

    public List<ProjectResponse> searchProjects(String term, String email) {
        User currentUser = getUserByEmail(email);
        List<ProjectResponse> result = new ArrayList<>();
        for (Project p : projectRepository.searchProjects(term)) result.add(mapToProjectResponse(p, currentUser));
        return result;
    }

    public ProjectResponse updateProject(Long projectId, String email, ProjectRequest request) {
        User user = getUserByEmail(email);
        Project project = getProjectById(projectId);
        if (!project.getLead().getId().equals(user.getId()))
            throw new RuntimeException("Only the project lead can update this project");
        if (request.getName() != null)        project.setName(request.getName());
        if (request.getDescription() != null) project.setDescription(request.getDescription());
        if (request.getSkills() != null)      project.setSkills(request.getSkills());
        if (request.getMaxMembers() != null)  project.setMaxMembers(request.getMaxMembers());
        return mapToProjectResponse(projectRepository.save(project), user);
    }

    public void deleteProject(Long projectId, String email) {
        User user = getUserByEmail(email);
        Project project = getProjectById(projectId);
        if (!project.getLead().getId().equals(user.getId()))
            throw new RuntimeException("Only the project lead can delete this project");
        for (ProjectFile f : project.getFiles()) fileStorageService.deleteFile(f.getFilePath());
        projectRepository.delete(project);
    }

    // ─────────────────────── JOIN REQUESTS ───────────────────

    public String requestToJoin(Long projectId, String email) {
        User user = getUserByEmail(email);
        Project project = getProjectById(projectId);
        for (User m : project.getMembers())
            if (m.getId().equals(user.getId())) throw new RuntimeException("You are already a member");
        if (joinRequestRepository.existsByUserAndProject(user, project))
            throw new RuntimeException("You already sent a join request");
        JoinRequest request = JoinRequest.builder()
                .user(user).project(project).status(JoinRequest.RequestStatus.PENDING).build();
        joinRequestRepository.save(request);

        // Notify the lead
        notificationService.notifyJoinRequest(user, project.getLead(), project);
        return "Join request sent successfully";
    }

    public List<JoinRequestResponse> getJoinRequests(Long projectId, String email) {
        User user = getUserByEmail(email);
        Project project = getProjectById(projectId);
        if (!project.getLead().getId().equals(user.getId()))
            throw new RuntimeException("Only the project lead can view join requests");
        List<JoinRequestResponse> result = new ArrayList<>();
        for (JoinRequest jr : joinRequestRepository.findByProjectAndStatus(project, JoinRequest.RequestStatus.PENDING))
            result.add(mapToJoinRequestResponse(jr));
        return result;
    }

    public String acceptJoinRequest(Long requestId, String email) {
        User lead = getUserByEmail(email);
        JoinRequest joinRequest = joinRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));
        Project project = joinRequest.getProject();
        if (!project.getLead().getId().equals(lead.getId()))
            throw new RuntimeException("Only the project lead can accept requests");
        project.getMembers().add(joinRequest.getUser());
        joinRequest.setStatus(JoinRequest.RequestStatus.ACCEPTED);
        projectRepository.save(project);
        joinRequestRepository.save(joinRequest);

        // Notify the user whose request was accepted
        notificationService.notifyRequestAccepted(lead, joinRequest.getUser(), project);
        return joinRequest.getUser().getName() + " has been added to the project";
    }

    public String declineJoinRequest(Long requestId, String email) {
        User lead = getUserByEmail(email);
        JoinRequest joinRequest = joinRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));
        if (!joinRequest.getProject().getLead().getId().equals(lead.getId()))
            throw new RuntimeException("Only the project lead can decline requests");
        joinRequest.setStatus(JoinRequest.RequestStatus.DECLINED);
        joinRequestRepository.save(joinRequest);
        return "Request declined";
    }

    public String kickMember(Long projectId, Long memberId, String email) {
        User lead = getUserByEmail(email);
        Project project = getProjectById(projectId);
        if (!project.getLead().getId().equals(lead.getId()))
            throw new RuntimeException("Only the project lead can remove members");
        if (lead.getId().equals(memberId))
            throw new RuntimeException("The lead cannot remove themselves");
        User memberToKick = userRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        project.getMembers().remove(memberToKick);
        projectRepository.save(project);
        return memberToKick.getName() + " has been removed from the project";
    }

    // ─────────────────────── STATUS ──────────────────────────

    /** Mark project as COMPLETED — requires at least 1 file uploaded */
    public ProjectResponse completeProject(Long projectId, String email) {
        User lead = getUserByEmail(email);
        Project project = getProjectById(projectId);
        if (!project.getLead().getId().equals(lead.getId()))
            throw new RuntimeException("Only the lead can mark the project as complete");
        if (project.getFiles().isEmpty())
            throw new RuntimeException("Cannot complete project without at least 1 uploaded file");
        project.setStatus(ProjectStatus.COMPLETED);
        return mapToProjectResponse(projectRepository.save(project), lead);
    }

    /** Soft-delete project — marks as DELETED without removing from DB */
    public void softDeleteProject(Long projectId, String email) {
        User lead = getUserByEmail(email);
        Project project = getProjectById(projectId);
        if (!project.getLead().getId().equals(lead.getId()))
            throw new RuntimeException("Only the lead can delete the project");
        project.setStatus(ProjectStatus.DELETED);
        projectRepository.save(project);
    }

    /** Reopen a completed project back to ACTIVE */
    public ProjectResponse reopenProject(Long projectId, String email) {
        User lead = getUserByEmail(email);
        Project project = getProjectById(projectId);
        if (!project.getLead().getId().equals(lead.getId()))
            throw new RuntimeException("Only the lead can reopen the project");
        project.setStatus(ProjectStatus.ACTIVE);
        return mapToProjectResponse(projectRepository.save(project), lead);
    }

    // ─────────────────────── FILES ───────────────────────────

    public FileResponse uploadFile(Long projectId, String email, MultipartFile file) throws IOException {
        User user = getUserByEmail(email);
        Project project = getProjectById(projectId);
        boolean isProjectLead = project.getLead().getId().equals(user.getId());
        boolean isMember = isProjectLead;
        if (!isMember) for (User m : project.getMembers()) if (m.getId().equals(user.getId())) { isMember = true; break; }
        if (!isMember) throw new RuntimeException("Only project members can upload files");
        String filePath = fileStorageService.saveProjectFile(file, projectId);
        ProjectFile projectFile = ProjectFile.builder()
                .fileName(file.getOriginalFilename()).filePath(filePath)
                .fileSize(file.getSize()).fileType(file.getContentType())
                .uploadedBy(user).project(project).build();
        return mapToFileResponse(projectFileRepository.save(projectFile));
    }

    public List<FileResponse> getProjectFiles(Long projectId, String email) {
        User user = getUserByEmail(email);
        Project project = getProjectById(projectId);
        boolean isProjectLead = project.getLead().getId().equals(user.getId());
        boolean isMember = isProjectLead;
        if (!isMember) for (User m : project.getMembers()) if (m.getId().equals(user.getId())) { isMember = true; break; }
        if (!isMember) throw new RuntimeException("Only project members can view files");
        List<FileResponse> result = new ArrayList<>();
        for (ProjectFile f : projectFileRepository.findByProject(project)) result.add(mapToFileResponse(f));
        return result;
    }

    public void deleteFile(Long fileId, String email) {
        User user = getUserByEmail(email);
        ProjectFile file = projectFileRepository.findById(fileId)
                .orElseThrow(() -> new RuntimeException("File not found"));
        boolean isUploader = file.getUploadedBy().getId().equals(user.getId());
        boolean isLead = file.getProject().getLead().getId().equals(user.getId());
        if (!isUploader && !isLead) throw new RuntimeException("No permission to delete this file");
        fileStorageService.deleteFile(file.getFilePath());
        projectFileRepository.delete(file);
    }

    // ─────────────────────── HELPERS ─────────────────────────

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));
    }

    private Project getProjectById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found: " + id));
    }

    private ProjectResponse mapToProjectResponse(Project project, User currentUser) {
        boolean isMember = false, isLead = project.getLead().getId().equals(currentUser.getId());
        for (User m : project.getMembers()) if (m.getId().equals(currentUser.getId())) { isMember = true; break; }
        boolean hasRequested = joinRequestRepository.existsByUserAndProject(currentUser, project);
        long pendingCount = 0;
        for (JoinRequest jr : project.getJoinRequests())
            if (jr.getStatus() == JoinRequest.RequestStatus.PENDING) pendingCount++;
        List<ProjectResponse.UserSummary> members = new ArrayList<>();
        for (User m : project.getMembers()) members.add(mapToUserSummary(m));
        return ProjectResponse.builder()
                .id(project.getId()).name(project.getName()).description(project.getDescription())
                .createdAt(project.getCreatedAt()).skills(project.getSkills())
                .lead(mapToUserSummary(project.getLead())).members(members)
                .pendingRequestsCount(isLead ? (int) pendingCount : null)
                .filesCount(project.getFiles().size()).maxMembers(project.getMaxMembers()).status(project.getStatus()).isMember(isMember)
                .isLead(isLead).hasRequested(hasRequested).build();
    }

    private ProjectResponse.UserSummary mapToUserSummary(User user) {
        return ProjectResponse.UserSummary.builder()
                .id(user.getId()).name(user.getName()).email(user.getEmail()).skills(user.getSkills())
                .profileImageUrl(user.getProfileImagePath() != null ? BASE_URL + user.getProfileImagePath() : null)
                .build();
    }

    private JoinRequestResponse mapToJoinRequestResponse(JoinRequest jr) {
        return JoinRequestResponse.builder()
                .id(jr.getId()).status(jr.getStatus()).requestedAt(jr.getRequestedAt())
                .userId(jr.getUser().getId()).userName(jr.getUser().getName())
                .userEmail(jr.getUser().getEmail()).userSkills(jr.getUser().getSkills())
                .userProfileImageUrl(jr.getUser().getProfileImagePath() != null
                        ? BASE_URL + jr.getUser().getProfileImagePath() : null)
                .build();
    }

    private FileResponse mapToFileResponse(ProjectFile file) {
        return FileResponse.builder()
                .id(file.getId()).fileName(file.getFileName()).fileUrl(BASE_URL + file.getFilePath())
                .fileSize(file.getFileSize()).fileType(file.getFileType())
                .uploadedAt(file.getUploadedAt()).uploadedByName(file.getUploadedBy().getName())
                .uploadedById(file.getUploadedBy().getId()).build();
    }
}