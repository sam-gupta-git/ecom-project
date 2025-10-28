package com.revature.training.ecommerce_project.services;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.revature.training.ecommerce_project.models.User;
import com.revature.training.ecommerce_project.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(String username, String password, String email) {
        String encodedPassword = passwordEncoder.encode(password);

        User user = new User(username, encodedPassword, email);
        return userRepository.save(user);
    }

    public User loginUser(String username, String password) {
        if (validatePassword(password, userRepository.findByUsername(username).getPassword())) {
            return userRepository.findByUsername(username);
        } else {
            return null;
        }
    }

    public User logoutUser() {
        return null;
    }

    public User getUserProfile(long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User updateUserProfile(long userId, String username, String email) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        user.setUsername(username);
        user.setEmail(email);
        return userRepository.save(user);
    }

    public void changePassword(long userId, String newRawPassword) {
        // Implement exception handling for correct response entity
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        String encodedPassword = passwordEncoder.encode(newRawPassword);
        user.setPassword(encodedPassword);

        userRepository.save(user);
    }

    public String generatePasswordResetToken(String email) {
        return UUID.randomUUID().toString();
    }

    public void resetPassword(String token, String email, String newPassword) {
        
    }

    public void getUserOrders(int userId) {
        
    }

    public void getWishlist(int userId) {
        
    }

    public boolean validatePassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
