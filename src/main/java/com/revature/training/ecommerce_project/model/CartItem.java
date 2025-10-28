package com.revature.training.ecommerce_project.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.FetchType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cart_items")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "item_id")
    private Item item;

    private int quantity;

    // Constructor without ID for creating new cart items
    public CartItem(String userId, Item item, int quantity) {
        this.userId = userId;
        this.item = item;
        this.quantity = quantity;
    }

    // Calculate total price for this cart item
    public double getTotalPrice() {
        return item.getPrice() * quantity;
    }
}