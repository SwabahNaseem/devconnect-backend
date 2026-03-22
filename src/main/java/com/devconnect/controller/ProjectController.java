package com.devconnect.controller;

import com.devconnect.dto.*;
import com.devconnect.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    public ResponseEntity<List<ProjectResponse>> getAllProjects(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(projectService.getAllProjects(userDetails.getUsername()));
    }

    @GetMapping("/mine")
    public ResponseEntity<List<ProjectResponse>> getMyProjects(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(projectService.getMyProjects(userDetails.getUsername()));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProjectResponse>> searchProjects(
            @RequestParam String term,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(projectService.searchProjects(term, userDetails.getUsername()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse> getProject(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(projectService.getProject(id, userDetails.getUsername()));
    }

    @PostMapping
    public ResponseEntity<ProjectResponse> createProject(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody ProjectRequest request) {
        return ResponseEntity.ok(projectService.createProject(userDetails.getUsername(), request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectResponse> updateProject(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody ProjectRequest request) {
        return ResponseEntity.ok(projectService.updateProject(id, userDetails.getUsername(), request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProject(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        projectService.deleteProject(id, userDetails.getUsername());
        return ResponseEntity.ok("Project deleted");
    }

    @PostMapping("/{id}/request")
    public ResponseEntity<String> requestToJoin(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(projectService.requestToJoin(id, userDetails.getUsername()));
    }

    @GetMapping("/{id}/requests")
    public ResponseEntity<List<JoinRequestResponse>> getJoinRequests(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(projectService.getJoinRequests(id, userDetails.getUsername()));
    }

    @PostMapping("/requests/{requestId}/accept")
    public ResponseEntity<String> acceptRequest(
            @PathVariable Long requestId,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(projectService.acceptJoinRequest(requestId, userDetails.getUsername()));
    }

    @PostMapping("/requests/{requestId}/decline")
    public ResponseEntity<String> declineRequest(
            @PathVariable Long requestId,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(projectService.declineJoinRequest(requestId, userDetails.getUsername()));
    }

    @DeleteMapping("/{projectId}/members/{memberId}")
    public ResponseEntity<String> kickMember(
            @PathVariable Long projectId,
            @PathVariable Long memberId,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(projectService.kickMember(projectId, memberId, userDetails.getUsername()));
    }

    @PostMapping("/{id}/files")
    public ResponseEntity<FileResponse> uploadFile(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(projectService.uploadFile(id, userDetails.getUsername(), file));
    }

    @GetMapping("/{id}/files")
    public ResponseEntity<List<FileResponse>> getFiles(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(projectService.getProjectFiles(id, userDetails.getUsername()));
    }

    @DeleteMapping("/files/{fileId}")
    public ResponseEntity<String> deleteFile(
            @PathVariable Long fileId,
            @AuthenticationPrincipal UserDetails userDetails) {
        projectService.deleteFile(fileId, userDetails.getUsername());
        return ResponseEntity.ok("File deleted");
    }
}