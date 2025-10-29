package com.revature.training.ecommerce_project.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.revature.training.ecommerce_project.model.Item;
import com.revature.training.ecommerce_project.repository.ItemRepository;

@Service
public class ItemDisplayService {

    public ItemRepository itemRepository;
    private String imageString = "This is an image";

    public Item getProductById(Item item) {
        return itemRepository.findById(item.getId())
            .orElseThrow(() -> new RuntimeException("Item not found"));
    }

    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    public String getProductImages(Item item) {
        return imageString;
    }

    public double getProductPricing(Item item) {
        return item.getPrice();
    }

}
