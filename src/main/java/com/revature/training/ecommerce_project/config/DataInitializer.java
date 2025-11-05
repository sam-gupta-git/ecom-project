package com.revature.training.ecommerce_project.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.revature.training.ecommerce_project.model.User;
import com.revature.training.ecommerce_project.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Initialize test users if they don't exist
        if (userRepository.findByUsername("testuser2") == null) {
            User testUser = new User("testuser2", passwordEncoder.encode("password"), "testuser2@gmail.com");
            userRepository.save(testUser);
            System.out.println("Created test user: testuser2");
        }
        
        // Sample item initialization removed - no longer auto-creating items
        System.out.println("DataInitializer completed - no sample items created");
    }
}