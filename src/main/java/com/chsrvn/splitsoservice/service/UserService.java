package com.chsrvn.splitsoservice.service;

import com.chsrvn.splitsoservice.config.JwtUtil;
import com.chsrvn.splitsoservice.entity.User;
import com.chsrvn.splitsoservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

        // Check if password is in last five, but since new user, lastFive is null
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getLastFivePasswords() == null) {
            user.setLastFivePasswords(new ArrayList<>());
        }
        user.getLastFivePasswords().add(user.getPassword());
        // Keep only last 5, but since new, it's 1

        return userRepository.save(user);
    }

    public String login(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty() || !passwordEncoder.matches(password, userOpt.get().getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
        // For JWT, need UserDetails, but since no roles, create a simple one
        return jwtUtil.generateToken(new org.springframework.security.core.userdetails.User(email, password, new ArrayList<>()));
    }

    public void changePassword(String email, String oldPassword, String newPassword) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty() || !passwordEncoder.matches(oldPassword, userOpt.get().getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
        User user = userOpt.get();
        if (user.getLastFivePasswords().contains(passwordEncoder.encode(newPassword))) {
            throw new RuntimeException("Password cannot be one of the last five passwords");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        user.getLastFivePasswords().add(user.getPassword());
        if (user.getLastFivePasswords().size() > 5) {
            user.getLastFivePasswords().remove(0);
        }
        userRepository.save(user);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }
}
