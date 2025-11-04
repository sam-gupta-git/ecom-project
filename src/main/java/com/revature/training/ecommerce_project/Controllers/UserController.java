package com.revature.training.ecommerce_project.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;
import java.util.HashMap;

import com.revature.training.ecommerce_project.services.UserService;
import com.revature.training.ecommerce_project.model.User;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity <Map <String, Object>> registerUser(
        @RequestParam String username, 
        @RequestParam String password, 
        @RequestParam String email) {
        User user = userService.registerUser(username, password, email);
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "User registered successfully.");
        response.put("userId", user.getId());
        response.put("username", user.getUsername());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity <Map <String, Object>> loginUser(
        @RequestBody User loginRequest) {
        User user = userService.loginUser(loginRequest.getUsername(), loginRequest.getPassword());

        Map<String, Object> response = new HashMap<>();
        response.put("messgae", "User logged-in successfully");
        response.put("user", user);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logoutUser() {
        userService.logoutUser();

        Map<String, String> response = new HashMap<>();
        response.put("message", "User logged-out successfully");
        return ResponseEntity.ok(response);
        // Might want to change this to return an empty session token? 
    }

    @GetMapping("/profile/{userId}")
    public ResponseEntity<User> getUserProfile(@PathVariable long userId) {
        User user = userService.getUserProfile(userId);

        return ResponseEntity.ok(user);
    }

    @PutMapping("/profile")
    public ResponseEntity<Map<String, Object>> updateUserProfile(@RequestBody User user) {
        User updatedUser = userService.updateUserProfile(user.getId(), user.getUsername(), user.getEmail());

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Profile updated successfully");
        response.put("user", updatedUser);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/change-password")
    public ResponseEntity<Map<String, String>> changePassword(
            @RequestParam long userId, 
            @RequestParam String newPassword) {
        userService.changePassword(userId, newPassword);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Password changed successfully");
        return ResponseEntity.ok(response);
    }

    public void forgotPassword(String req, String res) {
        
    }

    @PostMapping("/reset-password") // Fixed mapping
    public ResponseEntity<Map<String, String>> resetPassword(
            @RequestParam String token, 
            @RequestParam String email, 
            @RequestParam String newPassword) {
        userService.resetPassword(token, email, newPassword);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Password reset successfully");
        return ResponseEntity.ok(response);
    }

    public void getWishlist(String req, String res) {
        
    }
}
