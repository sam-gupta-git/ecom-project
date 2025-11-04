package com.revature.training.ecommerce_project.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "order_history")
public class OrderHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // Foreign key to User
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({"orderHistories", "password"})
    private User user;
    
    // Foreign key to Item
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;
    
    private String orderNumber;
    private LocalDateTime orderDate;
    private int quantity;
    private double itemPrice; // Price at time of purchase
    private double totalItemPrice; // itemPrice * quantity
    private BigDecimal orderTotalAmount; // Total amount for the entire order (for grouping)
    
    // Constructor for creating order history entries
    public OrderHistory(User user, Item item, String orderNumber, LocalDateTime orderDate, 
                       int quantity, double itemPrice, BigDecimal orderTotalAmount) {
        this.user = user;
        this.item = item;
        this.orderNumber = orderNumber;
        this.orderDate = orderDate;
        this.quantity = quantity;
        this.itemPrice = itemPrice;
        this.totalItemPrice = itemPrice * quantity;
        this.orderTotalAmount = orderTotalAmount;
    }
}

