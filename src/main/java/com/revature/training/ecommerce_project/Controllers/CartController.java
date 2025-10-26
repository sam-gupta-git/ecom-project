package com.revature.training.ecommerce_project.controllers;
import org.springframework.web.bind.annotation.RestController;

import com.revature.training.ecommerce_project.services.CartService;
import com.revature.training.ecommerce_project.services.CheckoutService;

@RestController
public class CartController {
    private final CartService cartService;
    private final CheckoutService checkoutService;

    public CartController(CartService cartService, CheckoutService checkoutService) {
        this.cartService = cartService;
        this.checkoutService = checkoutService;
    }
    
    public void addToCart(String userID, int itemID, int quantity) {
        cartService.addToCart(userID, itemID, quantity);
    }

    public void removeFromCart(String userID, int itemID) {
        cartService.removeFromCart(userID, itemID);
    }

    public void updateItemQuantity(String userID, int itemID, int newQuantity) {
        cartService.updateItemQuantity(userID, itemID, newQuantity);
    }

    public void viewCart(String userID) {
        cartService.viewCart(userID);
    }

    public void clearCart(String userID) {
        cartService.clearCart(userID);
    }

    public void applyDiscountCode(String userID, String code) {
        checkoutService.applyDiscountCode(userID, code);
    }

    public void removeDiscountCode(String userID) {
        checkoutService.removeDiscountCode(userID);
    }
}
