package com.revature.training.ecommerce_project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.revature.training.ecommerce_project.model.CartItem;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
public class CheckoutService {
    
    @Autowired
    private CartService cartService;

    // In-memory discount storage for simplicity (in production, use database)
    private Map<String, Double> userDiscounts = new HashMap<>();
    
    // Tax rate (8.5%)
    private static final double TAX_RATE = 0.085;
    
    // Shipping cost
    private static final double SHIPPING_COST = 9.99;
    
    public void validateOrder(String userID) {
        if (cartService.isEmpty(userID)) {
            throw new IllegalArgumentException("Cannot checkout with empty cart");
        }
        
        List<CartItem> cartItems = cartService.viewCart(userID);
        for (CartItem item : cartItems) {
            if (item.getQuantity() > item.getItem().getStockQuantity()) {
                throw new IllegalArgumentException("Insufficient stock for item: " + item.getItem().getName());
            }
        }
    }

    public double calculateTotal(String userID) {
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

        if (cartService.isEmpty(userID)) {
            throw new IllegalArgumentException("Cannot apply discount to empty cart");
        }

        userDiscounts.put(userID, discountCodes.get(code.toUpperCase()));
    }

    public void removeDiscountCode(String userID) {
        userDiscounts.remove(userID);
    }

    public void setShippingMethod(String userID, String shippingMethod, String address) {
        // TODO: Implement shipping method selection
        // This could store different shipping options and costs
    }

    public void setPaymentMethod(String userID, String paymentMethod) {
        // TODO: Implement payment method selection
        // This could validate and store payment information
    }

    public double calculateTaxes(String userID) {
        double subtotal = getSubtotal(userID);
        double discount = userDiscounts.getOrDefault(userID, 0.0);
        double discountedSubtotal = subtotal - (subtotal * discount);
        return discountedSubtotal * TAX_RATE;
    }

    public double calculateShippingCost(String userID) {
        double subtotal = getSubtotal(userID);
        double discount = userDiscounts.getOrDefault(userID, 0.0);
        double discountedSubtotal = subtotal - (subtotal * discount);
        return discountedSubtotal >= 50.0 ? 0.0 : SHIPPING_COST;
    }

    public void finalizeOrder(String userID, int checkoutDataID) {
        validateOrder(userID);
        // TODO: Create order record, update inventory, clear cart
    }

    public void processPayment(String userID, int orderID, int paymentDataID) {
        // TODO: Integrate with payment processor
    }

    public void sendOrderConfirmation(String userID, int orderID) {
        // TODO: Send email confirmation
    }

    public Map<String, Object> getCheckoutSummary(String userID) {
        Map<String, Object> summary = new HashMap<>();
        
        double subtotal = getSubtotal(userID);
        double discountAmount = getDiscountAmount(userID);
        double tax = calculateTaxes(userID);
        double shipping = calculateShippingCost(userID);
        double total = calculateTotal(userID);
        
        summary.put("subtotal", subtotal);
        summary.put("discountAmount", discountAmount);
        summary.put("tax", tax);
        summary.put("shipping", shipping);
        summary.put("total", total);
        summary.put("itemCount", cartService.getTotalItemCount(userID));
        summary.put("appliedDiscountCode", getAppliedDiscountCode(userID));
        summary.put("freeShipping", shipping == 0.0);
        
        return summary;
    }

    // Helper methods moved from CartService
    
    private double getSubtotal(String userID) {
        return cartService.getSubtotal(userID);
    }

    public double getDiscountAmount(String userID) {
        double subtotal = getSubtotal(userID);
        double discount = userDiscounts.getOrDefault(userID, 0.0);
        return subtotal * discount;
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
}
