package com.devconnect.service;

import com.devconnect.dto.AuthResponse;
import com.devconnect.dto.LoginRequest;
import com.devconnect.dto.RegisterRequest;
import com.devconnect.model.User;
import com.devconnect.repository.UserRepository;
import com.devconnect.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil,
                       AuthenticationManager authenticationManager) {
        this.userRepository        = userRepository;
        this.passwordEncoder       = passwordEncoder;
        this.jwtUtil               = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered. Please login.");
        }
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        User saved = userRepository.save(user);
        String token = jwtUtil.generateToken(saved.getEmail());

        AuthResponse res = new AuthResponse();
        res.setToken(token);
        res.setUserId(saved.getId());
        res.setName(saved.getName());
        res.setEmail(saved.getEmail());
        return res;
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(), request.getPassword()));

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtUtil.generateToken(user.getEmail());

        AuthResponse res = new AuthResponse();
        res.setToken(token);
        res.setUserId(user.getId());
        res.setName(user.getName());
        res.setEmail(user.getEmail());
        res.setProfileImagePath(user.getProfileImagePath());
        return res;
    }
}