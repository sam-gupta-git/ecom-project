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
import com.revature.training.ecommerce_project.model.CartItem;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestBody;

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
    public ResponseEntity<Map<String, Object>> addToCart(
            @PathVariable String userID, 
            @RequestParam(value = "itemID", required = false) Integer itemID,
            @RequestParam(value = "itemId", required = false) Integer itemId,
            @RequestParam int quantity) {
        try {
            // Support both itemID and itemId parameter names
            int actualItemId = (itemID != null) ? itemID : (itemId != null) ? itemId : 0;
            
            if (actualItemId == 0) {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "error", "Item ID is required (use either itemID or itemId parameter)"
                ));
            }
            
            cartService.addToCart(userID, actualItemId, quantity);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Item added to cart",
                "userId", userID,
                "itemId", actualItemId,
                "quantity", quantity
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "error", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                "success", false,
                "error", "Failed to add item to cart: " + e.getMessage()
            ));
        }
    }

    @PostMapping("/add-json/{userID}")
    public ResponseEntity<Map<String, Object>> addToCartJson(
            @PathVariable String userID, 
            @RequestBody Map<String, Object> requestBody) {
        try {
            // Support various parameter names from JSON body
            Integer itemId = null;
            Integer quantity = null;
            
            // Try different parameter names
            if (requestBody.containsKey("itemId")) {
                itemId = (Integer) requestBody.get("itemId");
            } else if (requestBody.containsKey("itemID")) {
                itemId = (Integer) requestBody.get("itemID");
            } else if (requestBody.containsKey("id")) {
                itemId = (Integer) requestBody.get("id");
            }
            
            if (requestBody.containsKey("quantity")) {
                quantity = (Integer) requestBody.get("quantity");
            }
            
            if (itemId == null || quantity == null) {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "error", "Both itemId and quantity are required in request body"
                ));
            }
            
            cartService.addToCart(userID, itemId, quantity);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Item added to cart",
                "userId", userID,
                "itemId", itemId,
                "quantity", quantity
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "error", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                "success", false,
                "error", "Failed to add item to cart: " + e.getMessage()
            ));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addToCartGeneric(@RequestBody Map<String, Object> requestBody) {
        try {
            // Extract username from JSON body
            String username = null;
            if (requestBody.containsKey("username")) {
                username = (String) requestBody.get("username");
            } else if (requestBody.containsKey("userId")) {
                username = (String) requestBody.get("userId");
            } else if (requestBody.containsKey("user")) {
                username = (String) requestBody.get("user");
            }
            
            if (username == null) {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "error", "Username is required in request body (use 'username', 'userId', or 'user' field)"
                ));
            }
            
            // Extract item ID
            Integer itemId = null;
            if (requestBody.containsKey("itemId")) {
                itemId = (Integer) requestBody.get("itemId");
            } else if (requestBody.containsKey("itemID")) {
                itemId = (Integer) requestBody.get("itemID");
            } else if (requestBody.containsKey("id")) {
                itemId = (Integer) requestBody.get("id");
            }
            
            if (itemId == null) {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "error", "Item ID is required in request body (use 'itemId', 'itemID', or 'id' field)"
                ));
            }
            
            // Extract quantity
            Integer quantity = null;
            if (requestBody.containsKey("quantity")) {
                quantity = (Integer) requestBody.get("quantity");
            }
            
            if (quantity == null) {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "error", "Quantity is required in request body"
                ));
            }
            
            cartService.addToCart(username, itemId, quantity);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Item added to cart",
                "username", username,
                "itemId", itemId,
                "quantity", quantity
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "error", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                "success", false,
                "error", "Failed to add item to cart: " + e.getMessage()
            ));
        }
    }

    @DeleteMapping("/remove/{userID}")
    public ResponseEntity<Map<String, Object>> removeFromCart(@PathVariable String userID, @RequestParam int itemID) {
        try {
            cartService.removeFromCart(userID, itemID);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Item removed from cart"
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "error", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                "success", false,
                "error", "Failed to remove item from cart"
            ));
        }
    }

    @PutMapping("/update/{userID}")
    public ResponseEntity<Map<String, Object>> updateItemQuantity(@PathVariable String userID, @RequestParam int itemID, @RequestParam int newQuantity) {
        try {
            cartService.updateItemQuantity(userID, itemID, newQuantity);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Item quantity updated"
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "error", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                "success", false,
                "error", "Failed to update item quantity"
            ));
        }
    }

    @GetMapping("/view/{userID}")
    public ResponseEntity<List<CartItem>> viewCart(@PathVariable String userID) {
        try {
            List<CartItem> cartItems = cartService.viewCart(userID);
            return ResponseEntity.ok(cartItems);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @DeleteMapping("/clear/{userID}")
    public ResponseEntity<Map<String, Object>> clearCart(@PathVariable String userID) {
        try {
            cartService.clearCart(userID);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Cart cleared"
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                "success", false,
                "error", "Failed to clear cart"
            ));
        }
    }

    @PostMapping("/discount/apply/{userID}")
    public ResponseEntity<Map<String, Object>> applyDiscountCode(@PathVariable String userID, @RequestParam String code) {
        try {
            checkoutService.applyDiscountCode(userID, code);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Discount code applied"
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "error", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                "success", false,
                "error", "Failed to apply discount code"
            ));
        }
    }

    @DeleteMapping("/discount/remove/{userID}")
    public ResponseEntity<Map<String, Object>> removeDiscountCode(@PathVariable String userID) {
        try {
            checkoutService.removeDiscountCode(userID);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Discount code removed"
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                "success", false,
                "error", "Failed to remove discount code"
            ));
        }
    }
}
