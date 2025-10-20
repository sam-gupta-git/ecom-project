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
import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
public class CartService {

    @Autowired
    private CartRepository cartRepository;
    
    @Autowired
    private ItemRepository itemRepository;

    // In-memory discount storage for simplicity (in production, use database)
    private Map<String, Double> userDiscounts = new HashMap<>();
    
    // Tax rate (8.5%)
    private static final double TAX_RATE = 0.085;
    
    // Shipping cost
    private static final double SHIPPING_COST = 9.99;

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
        // Also clear any applied discounts
        userDiscounts.remove(userID);
    }

    // Total without taxes, shipping, or discounts
    public double getSubtotal(String userID) {
        List<CartItem> cartItems = cartRepository.findByUserId(userID);
        return cartItems.stream()
                .mapToDouble(CartItem::getTotalPrice)
                .sum();
    }

    // Total with taxes, shipping, and discounts
    public double getTotalPrice(String userID) {
        double subtotal = getSubtotal(userID);
        
        if (subtotal == 0) {
            return 0; // Empty cart
        }

        // Apply discount if any
        double discount = userDiscounts.getOrDefault(userID, 0.0);
        double discountedSubtotal = subtotal - (subtotal * discount);

        // Add tax
        double tax = discountedSubtotal * TAX_RATE;
        
        // Add shipping (free shipping for orders over $50)
        double shipping = discountedSubtotal >= 50.0 ? 0.0 : SHIPPING_COST;

        return discountedSubtotal + tax + shipping;
    }

    public boolean hasItem(String userID, int itemID) {
        return cartRepository.findByUserIdAndItemId(userID, (long) itemID).isPresent();
    }

    public boolean isEmpty(String userID) {
        return !cartRepository.existsByUserId(userID);
    }

    public void applyDiscountCode(String userID, String code) {
        // Simple discount codes (in production, store in database)
        Map<String, Double> discountCodes = Map.of(
            "SAVE10", 0.10,    // 10% off
            "SAVE20", 0.20,    // 20% off
            "WELCOME5", 0.05,  // 5% off
            "STUDENT15", 0.15  // 15% off
        );

        if (!discountCodes.containsKey(code.toUpperCase())) {
            throw new IllegalArgumentException("Invalid discount code");
        }

        if (isEmpty(userID)) {
            throw new IllegalArgumentException("Cannot apply discount to empty cart");
        }

        userDiscounts.put(userID, discountCodes.get(code.toUpperCase()));
    }

    public void removeDiscountCode(String userID) {
        userDiscounts.remove(userID);
    }

    // Additional utility methods - possibly move to checkoutservice
    
    public int getTotalItemCount(String userID) {
        Integer count = cartRepository.getTotalItemCount(userID);
        return count != null ? count : 0;
    }

    public double getDiscountAmount(String userID) {
        double subtotal = getSubtotal(userID);
        double discount = userDiscounts.getOrDefault(userID, 0.0);
        return subtotal * discount;
    }

    public double getTax(String userID) {
        double subtotal = getSubtotal(userID);
        double discount = userDiscounts.getOrDefault(userID, 0.0);
        double discountedSubtotal = subtotal - (subtotal * discount);
        return discountedSubtotal * TAX_RATE;
    }

    public double getShipping(String userID) {
        double subtotal = getSubtotal(userID);
        double discount = userDiscounts.getOrDefault(userID, 0.0);
        double discountedSubtotal = subtotal - (subtotal * discount);
        return discountedSubtotal >= 50.0 ? 0.0 : SHIPPING_COST;
    }

    public String getAppliedDiscountCode(String userID) {
        if (!userDiscounts.containsKey(userID)) {
            return null;
        }
        
        double discountRate = userDiscounts.get(userID);
        // Return the discount code based on rate (simplified approach)
        if (discountRate == 0.10) return "SAVE10";
        if (discountRate == 0.20) return "SAVE20";
        if (discountRate == 0.05) return "WELCOME5";
        if (discountRate == 0.15) return "STUDENT15";
        
        return "CUSTOM_" + (int)(discountRate * 100) + "%";
    }

    public void getCartSummary(String userID) {
        
    }


}
