package com.revature.training.ecommerce_project.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.revature.training.ecommerce_project.model.Item;
import com.revature.training.ecommerce_project.repository.ItemRepository;
import com.revature.training.ecommerce_project.repository.WishlistRepository;
import com.revature.training.ecommerce_project.repository.OrderHistoryRepository;

@Service
public class ItemDisplayService {

    private final ItemRepository itemRepository;
    private final WishlistRepository wishlistRepository;
    private final OrderHistoryRepository orderHistoryRepository;
    private String imageString = "This is an image";

    public ItemDisplayService(ItemRepository itemRepository, WishlistRepository wishlistRepository, OrderHistoryRepository orderHistoryRepository) {
        this.itemRepository = itemRepository;
        this.wishlistRepository = wishlistRepository;
        this.orderHistoryRepository = orderHistoryRepository;
    }

    public Item findProductById(Item item) {
        return itemRepository.findById(item.getId())
            .orElseThrow(() -> new RuntimeException("Item not found"));
    }

    public List<Item> findAllItems() {
        return itemRepository.findAll();
    }

    public String findProductImages(Item item) {
        return imageString;
    }

    public double findProductPricing(Item item) {
        return item.getPrice();
    }

    public List<Item> findItemsSortedByPriceAsc () {
        return itemRepository.findAllByOrderByPriceAsc();
    }

    public List<Item> findItemsSortedByPriceDesc () {
        return itemRepository.findAllByOrderByPriceDesc();
    }

    public List<Item> findItemsSortedByNameAsc () {
        return itemRepository.findAllByOrderByNameAsc();
    }

    public List<Item> findItemsSortedByNameDesc () {
        return itemRepository.findAllByOrderByNameDesc();
    }

    public List<Item> findItemsByCreatedBy(Long userId) {
        return itemRepository.findByCreatedBy(userId);
    }

    public Item createItem(Item item) {
        // Ensure ID is not set (auto-generated)
        item.setId(0);
        
        // Set default image URL if not provided
        if (item.getImageUrl() == null || item.getImageUrl().trim().isEmpty()) {
            item.setImageUrl(null); // Will display placeholder in frontend
        }
        
        // Set default category if not provided
        if (item.getCategory() == null || item.getCategory().trim().isEmpty()) {
            item.setCategory("general");
        }
        
        // Save and return the created item
        return itemRepository.save(item);
    }

    public Item findItemById(Long id) {
        return itemRepository.findById(id).orElse(null);
    }

    @Transactional
    public void deleteItem(Long id) {
        try {
            // First, find the item to make sure it exists
            Item item = findItemById(id);
            if (item == null) {
                throw new RuntimeException("Item not found with id: " + id);
            }
            
            // Check if item is in any wishlist
            System.out.println("🔍 Checking if item is in any wishlist: " + id);
            boolean isInWishlist = wishlistRepository.existsByItemId(id);
            
            if (isInWishlist) {
                System.out.println("⚠️ Cannot delete item - it's in someone's wishlist");
                throw new RuntimeException("ITEM_IN_WISHLIST");
            }
            
            // Check if item has been ordered
            System.out.println("🔍 Checking if item has been ordered: " + id);
            boolean isInOrders = orderHistoryRepository.existsByItemId(id);
            
            if (isInOrders) {
                System.out.println("⚠️ Cannot delete item - it has been ordered");
                throw new RuntimeException("ITEM_IN_ORDERS");
            }
            
            // Item is not in any wishlist and has not been ordered, safe to delete
            System.out.println("🗑️ Deleting item with ID: " + id);
            itemRepository.deleteById(id);
            
            System.out.println("✅ Successfully deleted item");
        } catch (Exception e) {
            System.err.println("❌ Error deleting item: " + e.getMessage());
            throw e; // Re-throw to preserve the original exception type
        }
    }

}
