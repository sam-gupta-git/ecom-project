package com.revature.training.ecommerce_project.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "items")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    private double price;

    private int stockQuantity;

    private String category;

    private String imageUrl;

    @Column(name = "created_by")
    private Long createdBy; // User ID who created this item
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Additional constructor for creating items without an ID
    public Item(String name, String description, double price, int stockQuantity, String category, String imageUrl) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.category = category;
        this.imageUrl = imageUrl;
        this.createdAt = LocalDateTime.now();
    }

    // Constructor with createdBy for user-specific items
    public Item(String name, String description, double price, int stockQuantity, String category, String imageUrl, Long createdBy) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.category = category;
        this.imageUrl = imageUrl;
        this.createdBy = createdBy;
        this.createdAt = LocalDateTime.now();
    }
}
