package com.revature.training.ecommerce_project.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
    public User loginUser(String username, String password) {
        return userService.loginUser(username, password);
    }

    public void logoutUser(String req, String res) {
        
    }

    public void getUserProfile(String req, String res) {
        
    }

    public void updateUserProfile(String req, String res) {
        
    }

    public void changePassword(String req, String res) {
        
    }

    public void forgotPassword(String req, String res) {
        
    }

    public void resetPassword(String req, String res) {
        
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
