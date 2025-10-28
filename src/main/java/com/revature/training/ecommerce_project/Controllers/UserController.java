package com.revature.training.ecommerce_project.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.revature.training.ecommerce_project.services.UserService;
import com.revature.training.ecommerce_project.models.User;

@RestController
@RequestMapping("/api")
public class UserController {

    private UserService userService;

    @PostMapping("/users")
    public void registerUser(@RequestParam String username, @RequestParam String password, @RequestParam String email) {
        userService.registerUser(username, password, email);
    }

    @GetMapping("users/login")
    public User loginUser(@RequestParam String username, @RequestParam String password) {
        return userService.loginUser(username, password);
    }

    @GetMapping("users/logout")
    public void logoutUser() {
        userService.logoutUser();
        // Might want to change this to return an empty session token? 
    }

    @GetMapping("users/profile")
    public User getUserProfile(@RequestParam long userId) {
        return userService.getUserProfile(userId);
    }

    @PostMapping("users/profile")
    public User updateUserProfile(@RequestBody User user) {
        return userService.updateUserProfile(user.getId(), user.getUsername(), user.getEmail());
    }

    @PostMapping("users/change-password")
    public ResponseEntity<String> changePassword(@RequestParam long userId, @RequestParam String newPassword) {
        userService.changePassword(userId, newPassword);
        return ResponseEntity.ok("Password changed successfully");
        
    }

    public void forgotPassword(String req, String res) {
        
    }

    @PostMapping("users/reset-password?token={token}&email={email}&newPassword={newPassword}")
    public void resetPassword(String token, String email, String newPassword) {
        userService.resetPassword(token, email, newPassword);
        
    }

    public void getUserOrders(String req, String res) {
        
    }

    public void getSavedAddresses(String req, String res) {
        
    }

    public void addorUpdateAddress(String req, String res) {
        
    }

    public void deleteAddress(String req, String res) {
        
    }

    public void getWishlist(String req, String res) {
        
    }
}
