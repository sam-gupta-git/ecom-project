package com.revature.training.ecommerce_project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.revature.training.ecommerce_project.model.Wishlist;
import com.revature.training.ecommerce_project.model.User;
import com.revature.training.ecommerce_project.model.Item;
import com.revature.training.ecommerce_project.repository.WishlistRepository;
import com.revature.training.ecommerce_project.repository.UserRepository;
import com.revature.training.ecommerce_project.repository.ItemRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class WishlistService {
    
    @Autowired
    private WishlistRepository wishlistRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ItemRepository itemRepository;
    
    /**
     * Add an item to user's wishlist
     */
    public Wishlist addToWishlist(String username, Long itemId, String notes) {
        // Find user
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found: " + username);
        }
        
        // Find item
        Optional<Item> itemOpt = itemRepository.findById(itemId);
        if (itemOpt.isEmpty()) {
            throw new IllegalArgumentException("Item not found with ID: " + itemId);
        }
        Item item = itemOpt.get();
        
        // Check if item is already in wishlist
        Optional<Wishlist> existingWishlistItem = wishlistRepository.findByUserAndItem(user, item);
        if (existingWishlistItem.isPresent()) {
            throw new IllegalArgumentException("Item is already in your wishlist");
        }
        
        // Create new wishlist entry
        Wishlist wishlistItem = new Wishlist(user, item, LocalDateTime.now(), notes);
        return wishlistRepository.save(wishlistItem);
    }
    
    /**
     * Add an item to user's wishlist without notes
     */
    public Wishlist addToWishlist(String username, Long itemId) {
        return addToWishlist(username, itemId, null);
    }
    
    /**
     * Remove an item from user's wishlist
     */
    public void removeFromWishlist(String username, Long itemId) {
        // Find user
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found: " + username);
        }
        
        // Find item
        Optional<Item> itemOpt = itemRepository.findById(itemId);
        if (itemOpt.isEmpty()) {
            throw new IllegalArgumentException("Item not found with ID: " + itemId);
        }
        Item item = itemOpt.get();
        
        // Find and delete wishlist item
        Optional<Wishlist> wishlistItem = wishlistRepository.findByUserAndItem(user, item);
        if (wishlistItem.isEmpty()) {
            throw new IllegalArgumentException("Item is not in your wishlist");
        }
        
        wishlistRepository.delete(wishlistItem.get());
    }
    
    /**
     * Get all wishlist items for a user
     */
    public List<Wishlist> getUserWishlist(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found: " + username);
        }
        
        return wishlistRepository.findByUserIdOrderByAddedDateDesc(user.getId());
    }
    
    /**
     * Check if an item is in user's wishlist
     */
    public boolean isItemInWishlist(String username, Long itemId) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return false;
        }
        
        return wishlistRepository.existsByUserIdAndItemId(user.getId(), itemId);
    }
    
    /**
     * Get wishlist item count for a user
     */
    public Long getWishlistCount(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found: " + username);
        }
        
        return wishlistRepository.countByUserId(user.getId());
    }
    
    /**
     * Clear entire wishlist for a user
     */
    public void clearWishlist(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found: " + username);
        }
        
        List<Wishlist> userWishlist = wishlistRepository.findByUser(user);
        wishlistRepository.deleteAll(userWishlist);
    }
    
    /**
     * Update notes for a wishlist item
     */
    public Wishlist updateWishlistItemNotes(String username, Long itemId, String notes) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found: " + username);
        }
        
        Optional<Item> itemOpt = itemRepository.findById(itemId);
        if (itemOpt.isEmpty()) {
            throw new IllegalArgumentException("Item not found with ID: " + itemId);
        }
        Item item = itemOpt.get();
        
        Optional<Wishlist> wishlistItem = wishlistRepository.findByUserAndItem(user, item);
        if (wishlistItem.isEmpty()) {
            throw new IllegalArgumentException("Item is not in your wishlist");
        }
        
        Wishlist wishlist = wishlistItem.get();
        wishlist.setNotes(notes);
        return wishlistRepository.save(wishlist);
    }
    
    /**
     * Move item from wishlist to cart (if cart service integration is needed)
     */
    public void moveToCart(String username, Long itemId) {
        // This method can be implemented if you want to integrate with CartService
        // For now, it just removes from wishlist
        removeFromWishlist(username, itemId);
        // TODO: Add integration with CartService to add item to cart
    }
}