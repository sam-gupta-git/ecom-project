package com.revature.training.ecommerce_project.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.revature.training.ecommerce_project.model.User;
import com.revature.training.ecommerce_project.model.Item;
import com.revature.training.ecommerce_project.repository.UserRepository;
import com.revature.training.ecommerce_project.repository.ItemRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ItemRepository itemRepository;
    
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
        
        // Initialize test items if they don't exist
        if (itemRepository.count() == 0) {
            Item item1 = new Item("test", "lol", 1.0, 10, "test", "image1.jpg");
            Item item2 = new Item("Sample Item 2", "Description 2", 15.99, 20, "Electronics", "image2.jpg");
            Item item3 = new Item("Sample Item 3", "Description 3", 25.50, 15, "Books", "image3.jpg");
            
            itemRepository.save(item1);
            itemRepository.save(item2);
            itemRepository.save(item3);
            System.out.println("Created test items");
        }
    }
}