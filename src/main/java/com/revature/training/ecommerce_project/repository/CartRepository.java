package com.revature.training.ecommerce_project.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.revature.training.ecommerce_project.model.CartItem;
import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<CartItem, Long> {
    
    // Find all cart items for a specific user
    List<CartItem> findByUserId(String userId);
    
    // Find a specific item in a user's cart
    Optional<CartItem> findByUserIdAndItemId(String userId, Long itemId);
    
    // Delete all items from a user's cart
    void deleteByUserId(String userId);
    
    // Check if user has any items in cart
    boolean existsByUserId(String userId);
    
    // Get total number of items in user's cart (sum of all quantities)
    @Query("SELECT COALESCE(SUM(ci.quantity), 0) FROM CartItem ci WHERE ci.userId = :userId")
    Integer getTotalItemCount(@Param("userId") String userId);
}