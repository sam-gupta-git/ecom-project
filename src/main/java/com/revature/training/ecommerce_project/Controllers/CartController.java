package com.revature.training.ecommerce_project.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.ResponseEntity;

import com.revature.training.ecommerce_project.services.CartService;
import com.revature.training.ecommerce_project.services.CheckoutService;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "*")
public class CartController {
    private final CartService cartService;
    private final CheckoutService checkoutService;

    public CartController(CartService cartService, CheckoutService checkoutService) {
        this.cartService = cartService;
        this.checkoutService = checkoutService;
    }
    
    @PostMapping("/add/{userID}")
    public ResponseEntity<String> addToCart(@PathVariable String userID, @RequestParam int itemID, @RequestParam int quantity) {
        cartService.addToCart(userID, itemID, quantity);
        return ResponseEntity.ok("Item added to cart");
    }

    @DeleteMapping("/remove/{userID}")
    public ResponseEntity<String> removeFromCart(@PathVariable String userID, @RequestParam int itemID) {
        cartService.removeFromCart(userID, itemID);
        return ResponseEntity.ok("Item removed from cart");
    }

    @PutMapping("/update/{userID}")
    public ResponseEntity<String> updateItemQuantity(@PathVariable String userID, @RequestParam int itemID, @RequestParam int newQuantity) {
        cartService.updateItemQuantity(userID, itemID, newQuantity);
        return ResponseEntity.ok("Item quantity updated");
    }

    @GetMapping("/view/{userID}")
    public ResponseEntity<String> viewCart(@PathVariable String userID) {
        cartService.viewCart(userID);
        return ResponseEntity.ok("Cart viewed");
    }

    @DeleteMapping("/clear/{userID}")
    public ResponseEntity<String> clearCart(@PathVariable String userID) {
        cartService.clearCart(userID);
        return ResponseEntity.ok("Cart cleared");
    }

    @PostMapping("/discount/apply/{userID}")
    public ResponseEntity<String> applyDiscountCode(@PathVariable String userID, @RequestParam String code) {
        checkoutService.applyDiscountCode(userID, code);
        return ResponseEntity.ok("Discount code applied");
    }

    @DeleteMapping("/discount/remove/{userID}")
    public ResponseEntity<String> removeDiscountCode(@PathVariable String userID) {
        checkoutService.removeDiscountCode(userID);
        return ResponseEntity.ok("Discount code removed");
    }
}
