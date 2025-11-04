package com.revature.training.ecommerce_project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.revature.training.ecommerce_project.services.OrderHistoryService;
import com.revature.training.ecommerce_project.model.OrderHistory;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderHistoryController {

    @Autowired
    private OrderHistoryService orderHistoryService;

    /**
     * Get all order history for a user
     */
    @GetMapping("/user/{username}")
    public ResponseEntity<List<OrderHistory>> getUserOrderHistory(@PathVariable String username) {
        try {
            List<OrderHistory> orderHistory = orderHistoryService.getOrderHistoryByUsername(username);
            return ResponseEntity.ok(orderHistory);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }

    /**
     * Get all order history for a user (alternative endpoint for frontend compatibility)
     */
    @GetMapping("/history/{username}")
    public ResponseEntity<List<OrderHistory>> getOrderHistory(@PathVariable String username) {
        try {
            List<OrderHistory> orderHistory = orderHistoryService.getOrderHistoryByUsername(username);
            return ResponseEntity.ok(orderHistory);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }

    /**
     * Get order summaries for a user (grouped by order number)
     */
    @GetMapping("/user/{username}/summaries")
    public ResponseEntity<List<Map<String, Object>>> getUserOrderSummaries(@PathVariable String username) {
        try {
            List<Map<String, Object>> orderSummaries = orderHistoryService.getOrderSummariesByUsername(username);
            return ResponseEntity.ok(orderSummaries);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }

    /**
     * Get details for a specific order
     */
    @GetMapping("/order/{orderNumber}")
    public ResponseEntity<List<OrderHistory>> getOrderDetails(@PathVariable String orderNumber) {
        try {
            List<OrderHistory> orderDetails = orderHistoryService.getOrderByOrderNumber(orderNumber);
            if (orderDetails.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(orderDetails);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }

    /**
     * Get details for a specific order by user
     */
    @GetMapping("/user/{username}/order/{orderNumber}")
    public ResponseEntity<List<OrderHistory>> getUserOrderDetails(
            @PathVariable String username, 
            @PathVariable String orderNumber) {
        try {
            List<OrderHistory> orderDetails = orderHistoryService.getOrderByOrderNumberAndUsername(orderNumber, username);
            if (orderDetails.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(orderDetails);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }

    /**
     * Get total amount spent by user
     */
    @GetMapping("/user/{username}/total-spent")
    public ResponseEntity<Map<String, Object>> getUserTotalSpent(@PathVariable String username) {
        try {
            var totalSpent = orderHistoryService.getTotalSpentByUsername(username);
            return ResponseEntity.ok(Map.of("totalSpent", totalSpent));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Internal server error"));
        }
    }
}