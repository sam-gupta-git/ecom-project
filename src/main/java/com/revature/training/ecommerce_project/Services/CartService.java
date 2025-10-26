package com.revature.training.ecommerce_project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.revature.training.ecommerce_project.model.CartItem;
import com.revature.training.ecommerce_project.model.Item;
import com.revature.training.ecommerce_project.repository.CartRepository;
import com.revature.training.ecommerce_project.repository.ItemRepository;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CartService {

    @Autowired
    private CartRepository cartRepository;
    
    @Autowired
    private ItemRepository itemRepository;

    public void addToCart(String userID, int itemID, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }

        Optional<Item> itemOpt = itemRepository.findById((long) itemID);
        if (itemOpt.isEmpty()) {
            throw new IllegalArgumentException("Item not found");
        }

        Item item = itemOpt.get();
        
        // Check if item has sufficient stock
        if (item.getStockQuantity() < quantity) {
            throw new IllegalArgumentException("Insufficient stock available");
        }

        // Check if item already exists in cart
        Optional<CartItem> existingCartItem = cartRepository.findByUserIdAndItemId(userID, (long) itemID);
        
        if (existingCartItem.isPresent()) {
            // Update quantity if item already in cart
            CartItem cartItem = existingCartItem.get();
            int newQuantity = cartItem.getQuantity() + quantity;
            
            if (newQuantity > item.getStockQuantity()) {
                throw new IllegalArgumentException("Total quantity exceeds available stock");
            }
            
            cartItem.setQuantity(newQuantity);
            cartRepository.save(cartItem);
        } else {
            // Add new item to cart
            CartItem newCartItem = new CartItem(userID, item, quantity);
            cartRepository.save(newCartItem);
        }
    }

    public void removeFromCart(String userID, int itemID) {
        Optional<CartItem> cartItemOpt = cartRepository.findByUserIdAndItemId(userID, (long) itemID);
        
        if (cartItemOpt.isPresent()) {
            cartRepository.delete(cartItemOpt.get());
        } else {
            throw new IllegalArgumentException("Item not found in cart");
        }
    }

    public void updateItemQuantity(String userID, int itemID, int newQuantity) {
        if (newQuantity <= 0) {
            // Remove item if quantity is 0 or negative
            removeFromCart(userID, itemID);
            return;
        }

        Optional<CartItem> cartItemOpt = cartRepository.findByUserIdAndItemId(userID, (long) itemID);
        
        if (cartItemOpt.isEmpty()) {
            throw new IllegalArgumentException("Item not found in cart");
        }

        CartItem cartItem = cartItemOpt.get();
        
        // Check stock availability
        if (newQuantity > cartItem.getItem().getStockQuantity()) {
            throw new IllegalArgumentException("Quantity exceeds available stock");
        }

        cartItem.setQuantity(newQuantity);
        cartRepository.save(cartItem);
    }

    public List<CartItem> viewCart(String userID) {
        return cartRepository.findByUserId(userID);
    }

    public void clearCart(String userID) {
        cartRepository.deleteByUserId(userID);
    }

    // Total without taxes, shipping, or discounts
    public double getSubtotal(String userID) {
        List<CartItem> cartItems = cartRepository.findByUserId(userID);
        return cartItems.stream()
                .mapToDouble(CartItem::getTotalPrice)
                .sum();
    }

    public boolean hasItem(String userID, int itemID) {
        return cartRepository.findByUserIdAndItemId(userID, (long) itemID).isPresent();
    }

    public boolean isEmpty(String userID) {
        return !cartRepository.existsByUserId(userID);
    }

    // Additional utility methods
    
    public int getTotalItemCount(String userID) {
        Integer count = cartRepository.getTotalItemCount(userID);
        return count != null ? count : 0;
    }
}
