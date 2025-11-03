package com.revature.training.ecommerce_project.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.revature.training.ecommerce_project.model.Item;
import com.revature.training.ecommerce_project.services.ItemDisplayService;

@RestController
@RequestMapping("/api")
public class ItemController {

    private ItemDisplayService itemDisplayService;

    @GetMapping("/items")
    public List<Item> getAllItems() {
        return itemDisplayService.findAllItems();
    }

    @GetMapping("/items/filtered/{sortBy}")
    public List<Item> getFilteredItems(@PathVariable String sortBy) {
        if (sortBy.equals("asc")) {
            return itemDisplayService.findItemsSortedByPriceAsc();
        } else if (sortBy.equals("desc")) {
            return itemDisplayService.findItemsSortedByPriceDesc();
        } else {
            return null;
        }
    }

    @GetMapping("/item/{itemId}")
    public Item getItem(Item item) {
        return itemDisplayService.findProductById(item);
    }
    
}
