package com.revature.training.ecommerce_project.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "items")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @Email(message = "Email is not valid")
    private String email;

    // Additional constructor for creating users without an ID
    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }
}