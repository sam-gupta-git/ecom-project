package com.revature.training.ecommerce_project.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.revature.training.ecommerce_project.model.Item;
import com.revature.training.ecommerce_project.repository.ItemRepository;

@Service
public class ItemDisplayService {

    private final ItemRepository itemRepository;
    private String imageString = "This is an image";

    public ItemDisplayService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
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

}
