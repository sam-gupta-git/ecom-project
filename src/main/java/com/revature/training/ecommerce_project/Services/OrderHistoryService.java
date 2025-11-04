package com.revature.training.ecommerce_project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.revature.training.ecommerce_project.model.OrderHistory;
import com.revature.training.ecommerce_project.model.User;
import com.revature.training.ecommerce_project.repository.OrderHistoryRepository;
import com.revature.training.ecommerce_project.repository.UserRepository;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.math.BigDecimal;

@Service
@Transactional
public class OrderHistoryService {
    
    @Autowired
    private OrderHistoryRepository orderHistoryRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    /**
     * Get all order history for a user by username
     */
    public List<OrderHistory> getOrderHistoryByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found: " + username);
        }
        return orderHistoryRepository.findByUserIdOrderByOrderDateDesc(user.getId());
    }
    
    /**
     * Get all order history for a user by user ID
     */
    public List<OrderHistory> getOrderHistoryByUserId(Long userId) {
        return orderHistoryRepository.findByUserIdOrderByOrderDateDesc(userId);
    }
    
    /**
     * Get order details by order number
     */
    public List<OrderHistory> getOrderByOrderNumber(String orderNumber) {
        return orderHistoryRepository.findByOrderNumber(orderNumber);
    }
    
    /**
     * Get order details by order number for a specific user
     */
    public List<OrderHistory> getOrderByOrderNumberAndUsername(String orderNumber, String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found: " + username);
        }
        return orderHistoryRepository.findByOrderNumberAndUser(orderNumber, user);
    }
    
    /**
     * Get unique order summaries for a user (grouped by order number)
     */
    public List<Map<String, Object>> getOrderSummariesByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found: " + username);
        }
        
        List<String> orderNumbers = orderHistoryRepository.findDistinctOrderNumbersByUserId(user.getId());
        
        return orderNumbers.stream().map(orderNumber -> {
            List<OrderHistory> orderItems = orderHistoryRepository.findByOrderNumber(orderNumber);
            
            Map<String, Object> orderSummary = new HashMap<>();
            orderSummary.put("orderNumber", orderNumber);
            orderSummary.put("orderDate", orderItems.get(0).getOrderDate());
            orderSummary.put("totalAmount", orderItems.get(0).getOrderTotalAmount());
            orderSummary.put("itemCount", orderItems.size());
            
            return orderSummary;
        }).collect(Collectors.toList());
    }
    
    /**
     * Calculate total spent by user
     */
    public BigDecimal getTotalSpentByUsername(String username) {
        List<OrderHistory> orders = getOrderHistoryByUsername(username);
        return orders.stream()
                .collect(Collectors.groupingBy(OrderHistory::getOrderNumber))
                .values()
                .stream()
                .map(orderItems -> orderItems.get(0).getOrderTotalAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}