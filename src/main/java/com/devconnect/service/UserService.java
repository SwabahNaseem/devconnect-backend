package com.devconnect.service;

import com.devconnect.dto.UserProfileResponse;
import com.devconnect.model.User;
import com.devconnect.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;

    private static final String BASE_URL = "http://localhost:8080/";

    // Manual constructor
    public UserService(UserRepository userRepository,
                       FileStorageService fileStorageService) {
        this.userRepository = userRepository;
        this.fileStorageService = fileStorageService;
    }

    public UserProfileResponse getUserProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
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

        User saved = userRepository.save(user);
        return mapToProfileResponse(saved);
    }

    public UserProfileResponse uploadProfileImage(String email, MultipartFile file) throws IOException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getProfileImagePath() != null) {
            fileStorageService.deleteFile(user.getProfileImagePath());
        }

        String path = fileStorageService.saveProfileImage(file, user.getId());
        user.setProfileImagePath(path);

        User saved = userRepository.save(user);
        return mapToProfileResponse(saved);
    }

    public List<UserProfileResponse> searchUsers(String name) {
        List<User> allUsers = userRepository.findAll();
        List<UserProfileResponse> result = new ArrayList<>();
        for (User u : allUsers) {
            if (u.getName().toLowerCase().contains(name.toLowerCase())) {
                result.add(mapToProfileResponse(u));
            }
        }
        return result;
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