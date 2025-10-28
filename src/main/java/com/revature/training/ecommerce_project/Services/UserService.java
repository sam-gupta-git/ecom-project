package com.revature.training.ecommerce_project.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.revature.training.ecommerce_project.models.User;
import com.revature.training.ecommerce_project.repositories.UserRepository;
import com.revature.training.ecommerce_project.Utilities.SecurityConfig;

@Service
public class UserService {

    @Autowired
    private UserRepository UserRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    public void registerUser(String username, String password, String email) {
        User user = new User(username, password, email);
        UserRepository.save(user);
    }

    public User loginUser(String username, String password) {
        return UserRepository.findByUsernameAndPassword(username, password);
    }

    public User logoutUser() {
        return null;
    }

    public User getUserProfile(long userId) {
        return UserRepository.findById((long) userId).orElse(null);
    }

    public User updateUserProfile(long userId, String username, String email) {
        User user = UserRepository.findById((long) userId).orElse(null);
        if (user != null) {
            user.setUsername(username);
            user.setEmail(email);
            return UserRepository.save(user);
        }
        return null;
    }

    public void changePassword(long userId, String newPassword) {
        // Implement exception handling for correct response entity
        User user = UserRepository.findById((long) userId).orElse(null);
        if (user != null) {
            user.setPassword(newPassword);
            UserRepository.save(user);
        }
    }

    public String generatePasswordResetToken(String email) {
        return UUID.randomUUID().toString();
    }

    public void resetPassword(String token, String email, String newPassword) {
        
    }

    public void getUserOrders(int userId) {
        
    }

    public void getSavedAddresses(int userId) {
        
    }

    public void deleteAddress(int userId, int addressId) {
        
    }

    public void getWishlist(int userId) {
        
    }
}
