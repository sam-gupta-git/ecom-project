package com.revature.training.ecommerce_project.repository;

import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.revature.training.ecommerce_project.model.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    // Filter Items by Price Ceiling
    @Query("SELECT FROM item WHERE price <= ?1")
    List<Item> findByCeilingPrice(double price);

    // Filter Items by Price Floor
    @Query("SELECT FROM item WHERE price >= ?1")
    List<Item> findByFloorPrice(double price);

    List<Item> findAllOrderByPriceAsc();
    List<Item> findAllOrderByPriceDesc();

    List<Item> findAllOrderByNameAsc();
    List<Item> findAllOrderByNameDesc();

}
