package com.revature.training.ecommerce_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.revature.training.ecommerce_project.models.User;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsernameAndPassword(String username, String password);
}
