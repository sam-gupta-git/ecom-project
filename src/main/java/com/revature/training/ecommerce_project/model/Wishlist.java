package com.revature.training.ecommerce_project.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "wishlist")
public class Wishlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // Foreign key to User
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({"wishlists", "orderHistories", "password"})
    private User user;
    
    // Foreign key to Item
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;
    
    @Column(name = "added_date", nullable = false)
    private LocalDateTime addedDate;
    
    // Optional: notes about the wishlist item
    @Column(name = "notes", length = 500)
    private String notes;
    
    // Constructor for creating wishlist entries
    public Wishlist(User user, Item item, LocalDateTime addedDate) {
        this.user = user;
        this.item = item;
        this.addedDate = addedDate;
    }
    
    // Constructor with notes
    public Wishlist(User user, Item item, LocalDateTime addedDate, String notes) {
        this.user = user;
        this.item = item;
        this.addedDate = addedDate;
        this.notes = notes;
    }
}