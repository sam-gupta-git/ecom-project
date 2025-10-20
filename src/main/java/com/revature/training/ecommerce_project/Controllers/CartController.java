package com.revature.training.ecommerce_project.controllers;
import org.springframework.web.bind.annotation.RestController;

import com.revature.training.ecommerce_project.services.CartService;

@RestController
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
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
        cartService.applyDiscountCode(userID, code);
    }

    public void removeDiscountCode(String userID) {
        cartService.removeDiscountCode(userID);
    }
}
