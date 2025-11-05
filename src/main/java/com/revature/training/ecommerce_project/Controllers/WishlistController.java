package com.revature.training.ecommerce_project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.revature.training.ecommerce_project.services.WishlistService;
import com.revature.training.ecommerce_project.model.Wishlist;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/wishlist")
@CrossOrigin(origins = "*")
public class WishlistController {

    @Autowired
    private WishlistService wishlistService;

    /**
     * Add an item to user's wishlist
     */
    @PostMapping("/add/{username}/{itemId}")
    public ResponseEntity<Map<String, Object>> addToWishlist(@PathVariable String username, 
                                                           @PathVariable Long itemId,
                                                           @RequestBody(required = false) Map<String, String> requestBody) {
        try {
            String notes = requestBody != null ? requestBody.get("notes") : null;
            Wishlist wishlistItem = wishlistService.addToWishlist(username, itemId, notes);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Item added to wishlist successfully",
                "wishlistItem", wishlistItem
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "error", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                "success", false,
                "error", "Failed to add item to wishlist"
            ));
        }
    }

    /**
     * Remove an item from user's wishlist
     */
    @DeleteMapping("/remove/{username}/{itemId}")
    public ResponseEntity<Map<String, Object>> removeFromWishlist(@PathVariable String username, 
                                                                @PathVariable Long itemId) {
        try {
            wishlistService.removeFromWishlist(username, itemId);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Item removed from wishlist successfully"
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "error", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                "success", false,
                "error", "Failed to remove item from wishlist"
            ));
        }
    }

    /**
     * Get all wishlist items for a user
     */
    @GetMapping("/user/{username}")
    public ResponseEntity<List<Wishlist>> getUserWishlist(@PathVariable String username) {
        try {
            List<Wishlist> wishlist = wishlistService.getUserWishlist(username);
            return ResponseEntity.ok(wishlist);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }

    /**
     * Check if an item is in user's wishlist
     */
    @GetMapping("/check/{username}/{itemId}")
    public ResponseEntity<Map<String, Object>> checkItemInWishlist(@PathVariable String username, 
                                                                 @PathVariable Long itemId) {
        try {
            boolean inWishlist = wishlistService.isItemInWishlist(username, itemId);
            
            return ResponseEntity.ok(Map.of(
                "inWishlist", inWishlist,
                "username", username,
                "itemId", itemId
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                "error", "Failed to check wishlist status"
            ));
        }
    }

    /**
     * Get wishlist count for a user
     */
    @GetMapping("/count/{username}")
    public ResponseEntity<Map<String, Object>> getWishlistCount(@PathVariable String username) {
        try {
            Long count = wishlistService.getWishlistCount(username);
            
            return ResponseEntity.ok(Map.of(
                "count", count,
                "username", username
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                "error", "Failed to get wishlist count"
            ));
        }
    }

    /**
     * Clear entire wishlist for a user
     */
    @DeleteMapping("/clear/{username}")
    public ResponseEntity<Map<String, Object>> clearWishlist(@PathVariable String username) {
        try {
            wishlistService.clearWishlist(username);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Wishlist cleared successfully"
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "error", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                "success", false,
                "error", "Failed to clear wishlist"
            ));
        }
    }

    /**
     * Update notes for a wishlist item
     */
    @PutMapping("/update-notes/{username}/{itemId}")
    public ResponseEntity<Map<String, Object>> updateWishlistItemNotes(@PathVariable String username, 
                                                                     @PathVariable Long itemId,
                                                                     @RequestBody Map<String, String> requestBody) {
        try {
            String notes = requestBody.get("notes");
            Wishlist updatedItem = wishlistService.updateWishlistItemNotes(username, itemId, notes);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Wishlist item notes updated successfully",
                "wishlistItem", updatedItem
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "error", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                "success", false,
                "error", "Failed to update wishlist item notes"
            ));
        }
    }

    /**
     * Move item from wishlist to cart
     */
    @PostMapping("/move-to-cart/{username}/{itemId}")
    public ResponseEntity<Map<String, Object>> moveToCart(@PathVariable String username, 
                                                        @PathVariable Long itemId) {
        try {
            wishlistService.moveToCart(username, itemId);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Item moved from wishlist to cart successfully"
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "error", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                "success", false,
                "error", "Failed to move item to cart"
            ));
        }
    }

    /**
     * Toggle item in wishlist (add if not present, remove if present)
     */
    @PostMapping("/toggle/{username}/{itemId}")
    public ResponseEntity<Map<String, Object>> toggleWishlistItem(@PathVariable String username, 
                                                                @PathVariable Long itemId,
                                                                @RequestBody(required = false) Map<String, String> requestBody) {
        try {
            boolean wasInWishlist = wishlistService.isItemInWishlist(username, itemId);
            
            if (wasInWishlist) {
                wishlistService.removeFromWishlist(username, itemId);
                return ResponseEntity.ok(Map.of(
                    "success", true,
                    "action", "removed",
                    "message", "Item removed from wishlist",
                    "inWishlist", false
                ));
            } else {
                String notes = requestBody != null ? requestBody.get("notes") : null;
                Wishlist wishlistItem = wishlistService.addToWishlist(username, itemId, notes);
                return ResponseEntity.ok(Map.of(
                    "success", true,
                    "action", "added",
                    "message", "Item added to wishlist",
                    "inWishlist", true,
                    "wishlistItem", wishlistItem
                ));
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "error", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                "success", false,
                "error", "Failed to toggle wishlist item"
            ));
        }
    }
}