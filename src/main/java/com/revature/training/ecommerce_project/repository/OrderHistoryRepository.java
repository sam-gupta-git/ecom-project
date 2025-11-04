package com.revature.training.ecommerce_project.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.revature.training.ecommerce_project.model.OrderHistory;
import com.revature.training.ecommerce_project.model.User;
import java.util.List;

@Repository
public interface OrderHistoryRepository extends JpaRepository<OrderHistory, Long> {
    
    // Find all order history for a specific user
    List<OrderHistory> findByUser(User user);
    
    // Find all order history for a specific user by user ID
    @Query("SELECT oh FROM OrderHistory oh WHERE oh.user.id = :userId ORDER BY oh.orderDate DESC")
    List<OrderHistory> findByUserIdOrderByOrderDateDesc(@Param("userId") Long userId);
    
    // Find order history by order number
    List<OrderHistory> findByOrderNumber(String orderNumber);
    
    // Find order history by order number and user
    List<OrderHistory> findByOrderNumberAndUser(String orderNumber, User user);
    
    // Get unique order numbers for a user (for order grouping)
    @Query("SELECT DISTINCT oh.orderNumber FROM OrderHistory oh WHERE oh.user.id = :userId ORDER BY oh.orderDate DESC")
    List<String> findDistinctOrderNumbersByUserId(@Param("userId") Long userId);
}