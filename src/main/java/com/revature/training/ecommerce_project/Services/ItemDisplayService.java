package com.revature.training.ecommerce_project.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.revature.training.ecommerce_project.model.Item;
import com.revature.training.ecommerce_project.repository.ItemRepository;

@Service
public class ItemDisplayService {

    private ItemRepository itemRepository;
    private String imageString = "This is an image";

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
        return itemRepository.findAllOrderByPriceAsc();
    }

    public List<Item> findItemsSortedByPriceDesc () {
        return itemRepository.findAllOrderByPriceAsc();
    }

}
