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
    
}
