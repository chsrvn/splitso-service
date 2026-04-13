package com.chsrvn.splitsoservice.service;

import com.chsrvn.splitsoservice.config.JwtUtil;
import com.chsrvn.splitsoservice.entity.User;
import com.chsrvn.splitsoservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public User register(@Valid User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }
        if (userRepository.findByPhone(user.getPhone()).isPresent()) {
            throw new RuntimeException("Phone already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getCurrency() == null) {
            user.setCurrency("INR");
        }

        return userRepository.save(user);
    }

    public String login(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty() || !passwordEncoder.matches(password, userOpt.get().getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
        return jwtUtil.generateToken(new org.springframework.security.core.userdetails.User(email, password, new ArrayList<>()));
    }

    public void changePassword(String email, String oldPassword, String newPassword) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty() || !passwordEncoder.matches(oldPassword, userOpt.get().getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
        User user = userOpt.get();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> getUserById(UUID id) {
        return userRepository.findById(id);
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }
}
