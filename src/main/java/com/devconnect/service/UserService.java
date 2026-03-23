package com.devconnect.service;

import com.devconnect.dto.UserProfileResponse;
import com.devconnect.model.User;
import com.devconnect.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;
    private final PasswordEncoder passwordEncoder;

    private static final String BASE_URL = "http://localhost:8080/";

    public UserService(UserRepository userRepository,
                       FileStorageService fileStorageService,
                       PasswordEncoder passwordEncoder) {
        this.userRepository    = userRepository;
        this.fileStorageService= fileStorageService;
        this.passwordEncoder   = passwordEncoder;
    }

    public UserProfileResponse getUserProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));
        return mapToProfileResponse(user);
    }

    public UserProfileResponse getMyProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return mapToProfileResponse(user);
    }

    public UserProfileResponse updateProfile(String email, UserProfileResponse request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (request.getName() != null)     user.setName(request.getName());
        if (request.getBio() != null)      user.setBio(request.getBio());
        if (request.getAge() != null)      user.setAge(request.getAge());
        if (request.getLinkedin() != null) user.setLinkedin(request.getLinkedin());
        if (request.getGithub() != null)   user.setGithub(request.getGithub());
        if (request.getSkills() != null)   user.setSkills(request.getSkills());

        return mapToProfileResponse(userRepository.save(user));
    }

    public UserProfileResponse uploadProfileImage(String email, MultipartFile file) throws IOException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Delete old image if exists
        if (user.getProfileImagePath() != null) {
            fileStorageService.deleteFile(user.getProfileImagePath());
        }

        String imagePath = fileStorageService.saveProfileImage(file, user.getId());
        user.setProfileImagePath(imagePath);
        return mapToProfileResponse(userRepository.save(user));
    }

    public List<UserProfileResponse> searchUsers(String name) {
        List<User> users;
        if (name == null || name.trim().isEmpty()) {
            users = userRepository.findAll();
        } else {
            users = userRepository.findByNameContainingIgnoreCase(name);
        }
        List<UserProfileResponse> result = new ArrayList<>();
        for (User u : users) result.add(mapToProfileResponse(u));
        return result;
    }

    public void changePassword(String email, String currentPassword, String newPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }
        if (newPassword.length() < 6) {
            throw new RuntimeException("New password must be at least 6 characters");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public void deleteAccount(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(user);
    }

    private UserProfileResponse mapToProfileResponse(User user) {
        return UserProfileResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .bio(user.getBio())
                .age(user.getAge())
                .linkedin(user.getLinkedin())
                .github(user.getGithub())
                .skills(user.getSkills())
                .profileImageUrl(
                    user.getProfileImagePath() != null
                        ? BASE_URL + user.getProfileImagePath()
                        : null
                )
                .build();
    }
}