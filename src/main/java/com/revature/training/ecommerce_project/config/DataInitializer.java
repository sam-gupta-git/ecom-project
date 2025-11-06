package com.revature.training.ecommerce_project.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        // DataInitializer setup complete - no test data creation
        System.out.println("DataInitializer completed - no test users or items created");
    }
}