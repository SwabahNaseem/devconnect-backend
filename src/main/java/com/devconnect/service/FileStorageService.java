package com.devconnect.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    @Value("${app.upload.dir}")
    private String uploadDir;

    // No constructor needed — only @Value field, Spring injects it directly

    public String saveProfileImage(MultipartFile file, Long userId) throws IOException {
        Path profileDir = Paths.get(uploadDir + "profiles/");
        Files.createDirectories(profileDir);

        String uniqueFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path destination = profileDir.resolve(uniqueFileName);
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

        return "uploads/profiles/" + uniqueFileName;
    }

    public String saveProjectFile(MultipartFile file, Long projectId) throws IOException {
        Path projectDir = Paths.get(uploadDir + "projects/" + projectId + "/");
        Files.createDirectories(projectDir);

        String uniqueFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path destination = projectDir.resolve(uniqueFileName);
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

        return "uploads/projects/" + projectId + "/" + uniqueFileName;
    }

    public void deleteFile(String filePath) {
        try {
            Path path = Paths.get(filePath);
            Files.deleteIfExists(path);
        } catch (IOException e) {
            System.err.println("Could not delete file: " + filePath);
        }
    }

    public Path getFilePath(String relativePath) {
        return Paths.get(relativePath);
    }
}