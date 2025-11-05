package com.revature.training.ecommerce_project.controllers;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.time.LocalDateTime;

import com.revature.training.ecommerce_project.model.Item;
import com.revature.training.ecommerce_project.services.ItemDisplayService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ItemController {

    private final ItemDisplayService itemDisplayService;

    public ItemController(ItemDisplayService itemDisplayService) {
        this.itemDisplayService = itemDisplayService;
    }

    @GetMapping("/items")
    public List<Item> getAllItems() {
        return itemDisplayService.findAllItems();
    }

    @GetMapping("/items/sorted/price/{sortBy}")
    public List<Item> getSortedItemsByPrice(@PathVariable String sortBy) {
        if (sortBy.equals("asc")) {
            return itemDisplayService.findItemsSortedByPriceAsc();
        } else if (sortBy.equals("desc")) {
            return itemDisplayService.findItemsSortedByPriceDesc();
        } else {
            return null;
        }
    }

    @GetMapping("/items/sorted/name/{sortBy}")
    public List<Item> getSortedItemsByName(@PathVariable String sortBy) {
        if (sortBy.equals("asc")) {
            return itemDisplayService.findItemsSortedByNameAsc();
        } else if (sortBy.equals("desc")) {
            return itemDisplayService.findItemsSortedByNameDesc();
        } else {
            return null;
        }
    }

    @GetMapping("/items/{itemId}")
    public Item getItem(@PathVariable Long itemId) {
        Item item = new Item();
        item.setId(itemId);
        return itemDisplayService.findProductById(item);
    }

    @GetMapping("/users/{userId}/items")
    public List<Item> getItemsByUser(@PathVariable Long userId) {
        return itemDisplayService.findItemsByCreatedBy(userId);
    }

    @PostMapping("/items")
    public ResponseEntity<Map<String, Object>> createItem(@RequestBody Item item, @RequestHeader(value = "X-User-ID", required = false) String userIdHeader) {
        try {
            // Debug logging
            System.out.println("🔍 Received X-User-ID header: " + userIdHeader);
            System.out.println("🔍 Header is null: " + (userIdHeader == null));
            System.out.println("🔍 Header is empty: " + (userIdHeader != null && userIdHeader.trim().isEmpty()));
            
            // Validate required fields
            if (item.getName() == null || item.getName().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Item name is required"));
            }
            if (item.getDescription() == null || item.getDescription().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Item description is required"));
            }
            if (item.getPrice() <= 0) {
                return ResponseEntity.badRequest().body(Map.of("error", "Item price must be greater than 0"));
            }
            if (item.getStockQuantity() < 0) {
                return ResponseEntity.badRequest().body(Map.of("error", "Stock quantity cannot be negative"));
            }

            // Set user who created the item
            if (userIdHeader != null && !userIdHeader.trim().isEmpty()) {
                try {
                    Long userId = Long.parseLong(userIdHeader);
                    item.setCreatedBy(userId);
                    System.out.println("✅ Set createdBy to: " + userId);
                } catch (NumberFormatException e) {
                    System.err.println("❌ Invalid user ID header: " + userIdHeader);
                }
            } else {
                System.err.println("⚠️ No valid user ID header received");
            }
            
            // Set creation timestamp
            item.setCreatedAt(LocalDateTime.now());

            // Create the item
            Item createdItem = itemDisplayService.createItem(item);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Item created successfully");
            response.put("item", createdItem);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Failed to create item: " + e.getMessage()));
        }
    }
    
    @DeleteMapping("/items/{id}")
    public ResponseEntity<?> deleteItem(@PathVariable Long id, @RequestHeader(value = "X-User-ID", required = false) String userIdHeader) {
        try {
            System.out.println("🗑️ DELETE request for item ID: " + id);
            System.out.println("🔑 User ID from header: " + userIdHeader);
            
            // Get the item to check ownership
            Item item = itemDisplayService.findItemById(id);
            if (item == null) {
                return ResponseEntity.notFound().build();
            }
            
            // Verify ownership - only the creator can delete their item
            if (userIdHeader != null && !userIdHeader.trim().isEmpty()) {
                try {
                    Long userId = Long.parseLong(userIdHeader);
                    if (!userId.equals(item.getCreatedBy())) {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "You can only delete items you created"));
                    }
                } catch (NumberFormatException e) {
                    return ResponseEntity.badRequest().body(Map.of("error", "Invalid user ID"));
                }
            } else {
                return ResponseEntity.badRequest().body(Map.of("error", "User ID header is required"));
            }
            
            // Delete the item
            itemDisplayService.deleteItem(id);
            
            System.out.println("✅ Item deleted successfully: " + id);
            return ResponseEntity.ok().body(Map.of("message", "Item deleted successfully"));
        } catch (RuntimeException e) {
            String errorMessage = e.getMessage();
            System.err.println("❌ Failed to delete item: " + errorMessage);
            
            if ("ITEM_IN_WISHLIST".equals(errorMessage)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                    "error", "ITEM_IN_WISHLIST",
                    "message", "Cannot delete item - it's currently in someone's wishlist"
                ));
            }
            
            if ("ITEM_IN_ORDERS".equals(errorMessage)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                    "error", "ITEM_IN_ORDERS",
                    "message", "Cannot delete item - it has been ordered by customers"
                ));
            }
            
            return ResponseEntity.internalServerError().body(Map.of("error", "Failed to delete item: " + errorMessage));
        } catch (Exception e) {
            System.err.println("❌ Failed to delete item: " + e.getMessage());
            return ResponseEntity.internalServerError().body(Map.of("error", "Failed to delete item: " + e.getMessage()));
        }
    }
    
}
