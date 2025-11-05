package com.revature.training.ecommerce_project.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.revature.training.ecommerce_project.model.Wishlist;
import com.revature.training.ecommerce_project.model.User;
import com.revature.training.ecommerce_project.model.Item;
import java.util.List;
import java.util.Optional;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    
    // Find all wishlist items for a specific user
    List<Wishlist> findByUser(User user);
    
    // Find all wishlist items for a specific user by user ID
    @Query("SELECT w FROM Wishlist w WHERE w.user.id = :userId ORDER BY w.addedDate DESC")
    List<Wishlist> findByUserIdOrderByAddedDateDesc(@Param("userId") Long userId);
    
    // Find wishlist item by user and item (to check if already in wishlist)
    Optional<Wishlist> findByUserAndItem(User user, Item item);
    
    // Find wishlist item by user ID and item ID
    @Query("SELECT w FROM Wishlist w WHERE w.user.id = :userId AND w.item.id = :itemId")
    Optional<Wishlist> findByUserIdAndItemId(@Param("userId") Long userId, @Param("itemId") Long itemId);
    
    // Delete wishlist item by user and item
    void deleteByUserAndItem(User user, Item item);
    
    // Count wishlist items for a user
    @Query("SELECT COUNT(w) FROM Wishlist w WHERE w.user.id = :userId")
    Long countByUserId(@Param("userId") Long userId);
    
    // Check if item exists in user's wishlist
    @Query("SELECT COUNT(w) > 0 FROM Wishlist w WHERE w.user.id = :userId AND w.item.id = :itemId")
    boolean existsByUserIdAndItemId(@Param("userId") Long userId, @Param("itemId") Long itemId);
    
    // Check if item exists in any wishlist
    @Query("SELECT COUNT(w) > 0 FROM Wishlist w WHERE w.item.id = :itemId")
    boolean existsByItemId(@Param("itemId") Long itemId);
}