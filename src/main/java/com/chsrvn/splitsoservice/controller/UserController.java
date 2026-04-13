package com.chsrvn.splitsoservice.controller;

import com.chsrvn.splitsoservice.entity.User;
import com.chsrvn.splitsoservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> getCurrentUser(Authentication authentication) {
        String email = authentication.getName();
        Optional<User> userOpt = userService.getUserByEmail(email);
        if (userOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        User user = userOpt.get();
        return ResponseEntity.ok(Map.of(
            "id", user.getId(),
            "name", user.getName(),
            "email", user.getEmail(),
            "phone", user.getPhone(),
            "avatarUrl", user.getAvatarUrl() != null ? user.getAvatarUrl() : "",
            "currency", user.getCurrency(),
            "createDttm", user.getCreateDttm(),
            "chgDttm", user.getChgDttm()
        ));
    }

    @PutMapping("/me")
    public ResponseEntity<Map<String, Object>> updateProfile(@RequestBody Map<String, String> updates, Authentication authentication) {
        String email = authentication.getName();
        Optional<User> userOpt = userService.getUserByEmail(email);
        if (userOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        User user = userOpt.get();
        if (updates.containsKey("name")) {
            user.setName(updates.get("name"));
        }
        if (updates.containsKey("phone")) {
            user.setPhone(updates.get("phone"));
        }
        if (updates.containsKey("avatarUrl")) {
            user.setAvatarUrl(updates.get("avatarUrl"));
        }
        if (updates.containsKey("currency")) {
            user.setCurrency(updates.get("currency"));
        }
        User saved = userService.updateUser(user);
        return ResponseEntity.ok(Map.of(
            "id", saved.getId(),
            "name", saved.getName(),
            "email", saved.getEmail(),
            "phone", saved.getPhone(),
            "avatarUrl", saved.getAvatarUrl() != null ? saved.getAvatarUrl() : "",
            "currency", saved.getCurrency()
        ));
    }

    @PostMapping("/change-password")
    public ResponseEntity<Map<String, String>> changePassword(@RequestBody Map<String, String> passwords, Authentication authentication) {
        String oldPassword = passwords.get("oldPassword");
        String newPassword = passwords.get("newPassword");
        userService.changePassword(authentication.getName(), oldPassword, newPassword);
        return ResponseEntity.ok(Map.of("message", "Password changed successfully"));
    }
}
