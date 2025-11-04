package com.revature.training.ecommerce_project.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.revature.training.ecommerce_project.model.User;
import com.revature.training.ecommerce_project.model.Item;
import com.revature.training.ecommerce_project.model.OrderHistory;
import com.revature.training.ecommerce_project.repository.UserRepository;
import com.revature.training.ecommerce_project.repository.OrderHistoryRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
public class OrderHistoryServiceTest {

    @Mock
    private OrderHistoryRepository orderHistoryRepository;
    
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private OrderHistoryService orderHistoryService;

    private User testUser;
    private Item testItem1;
    private Item testItem2;
    private OrderHistory orderHistory1;
    private OrderHistory orderHistory2;

    @BeforeEach
    void setUp() {
        testUser = new User("testuser", "password", "test@email.com");
        testUser.setId(1L);
        
        testItem1 = new Item("Test Item 1", "Description 1", 10.0, 100, "Category", "image1.jpg");
        testItem1.setId(1L);
        
        testItem2 = new Item("Test Item 2", "Description 2", 20.0, 50, "Category", "image2.jpg");
        testItem2.setId(2L);
        
        orderHistory1 = new OrderHistory(testUser, testItem1, "ORD-12345678", 
                                       LocalDateTime.now(), 2, 10.0, BigDecimal.valueOf(40.0));
        orderHistory1.setId(1L);
        
        orderHistory2 = new OrderHistory(testUser, testItem2, "ORD-12345678", 
                                       LocalDateTime.now(), 1, 20.0, BigDecimal.valueOf(40.0));
        orderHistory2.setId(2L);
    }

    @Test
    void testGetOrderHistoryByUsername_Success() {
        // Arrange
        String username = "testuser";
        List<OrderHistory> expectedOrders = Arrays.asList(orderHistory1, orderHistory2);
        
        when(userRepository.findByUsername(username)).thenReturn(testUser);
        when(orderHistoryRepository.findByUserIdOrderByOrderDateDesc(testUser.getId())).thenReturn(expectedOrders);
        
        // Act
        List<OrderHistory> result = orderHistoryService.getOrderHistoryByUsername(username);
        
        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(expectedOrders, result);
    }

    @Test
    void testGetOrderHistoryByUsername_UserNotFound() {
        // Arrange
        String username = "nonexistent";
        when(userRepository.findByUsername(username)).thenReturn(null);
        
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> orderHistoryService.getOrderHistoryByUsername(username)
        );
        
        assertEquals("User not found: nonexistent", exception.getMessage());
    }

    @Test
    void testGetOrderSummariesByUsername_Success() {
        // Arrange
        String username = "testuser";
        List<String> orderNumbers = Arrays.asList("ORD-12345678");
        List<OrderHistory> orderItems = Arrays.asList(orderHistory1, orderHistory2);
        
        when(userRepository.findByUsername(username)).thenReturn(testUser);
        when(orderHistoryRepository.findDistinctOrderNumbersByUserId(testUser.getId())).thenReturn(orderNumbers);
        when(orderHistoryRepository.findByOrderNumber("ORD-12345678")).thenReturn(orderItems);
        
        // Act
        List<Map<String, Object>> result = orderHistoryService.getOrderSummariesByUsername(username);
        
        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        
        Map<String, Object> summary = result.get(0);
        assertEquals("ORD-12345678", summary.get("orderNumber"));
        assertEquals(BigDecimal.valueOf(40.0), summary.get("totalAmount"));
        assertEquals(2, summary.get("itemCount"));
    }

    @Test
    void testGetTotalSpentByUsername_Success() {
        // Arrange
        String username = "testuser";
        List<OrderHistory> orders = Arrays.asList(orderHistory1, orderHistory2);
        
        when(userRepository.findByUsername(username)).thenReturn(testUser);
        when(orderHistoryRepository.findByUserIdOrderByOrderDateDesc(testUser.getId())).thenReturn(orders);
        
        // Act
        BigDecimal result = orderHistoryService.getTotalSpentByUsername(username);
        
        // Assert
        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(40.0), result);
    }
}