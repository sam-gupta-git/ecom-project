package com.revature.training.ecommerce_project.services;

import com.revature.training.ecommerce_project.models.User;
import com.revature.training.ecommerce_project.repositories.UserRepository;

public class UserService {
    private UserRepository UserRepository;

    public void registerUser(User user) {
        
    }

    public User loginUser(String username, String password) {
        return UserRepository.findByUsernameAndPassword(username, password);
    }

    public void logoutUser(int userId) {
        
    }

    public void getUserProfile(int userId) {
        
    }

    public void updateUserProfile(int userId, String updatedData) {
        
    }

    public void changePassword(int userId, String oldPassword, String newPassword) {
        
    }

    public void generatePasswordResetToken(String email) {
        
    }

    public void resetPassword(String token, String newPassword) {
        
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
