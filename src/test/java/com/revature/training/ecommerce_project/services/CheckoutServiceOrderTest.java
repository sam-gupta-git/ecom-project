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
import com.revature.training.ecommerce_project.model.CartItem;
import com.revature.training.ecommerce_project.model.OrderHistory;
import com.revature.training.ecommerce_project.repository.UserRepository;
import com.revature.training.ecommerce_project.repository.OrderHistoryRepository;
import com.revature.training.ecommerce_project.repository.ItemRepository;

import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class CheckoutServiceOrderTest {

    @Mock
    private CartService cartService;
    
    @Mock
    private UserRepository userRepository;
    
    @Mock
    private OrderHistoryRepository orderHistoryRepository;
    
    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private CheckoutService checkoutService;

    private User testUser;
    private Item testItem1;
    private Item testItem2;
    private CartItem cartItem1;
    private CartItem cartItem2;

    @BeforeEach
    void setUp() {
        testUser = new User("testuser", "password", "test@email.com");
        testUser.setId(1L);
        
        testItem1 = new Item("Test Item 1", "Description 1", 10.0, 100, "Category", "image1.jpg");
        testItem1.setId(1L);
        
        testItem2 = new Item("Test Item 2", "Description 2", 20.0, 50, "Category", "image2.jpg");
        testItem2.setId(2L);
        
        cartItem1 = new CartItem("testuser", testItem1, 2);
        cartItem2 = new CartItem("testuser", testItem2, 1);
    }

    @Test
    void testFinalizeOrder_Success() {
        // Arrange
        String userId = "testuser";
        List<CartItem> cartItems = Arrays.asList(cartItem1, cartItem2);
        
        when(userRepository.findByUsername(userId)).thenReturn(testUser);
        when(cartService.viewCart(userId)).thenReturn(cartItems);
        when(cartService.isEmpty(userId)).thenReturn(false);
        when(cartService.getSubtotal(userId)).thenReturn(40.0); // 2*10 + 1*20
        
        // Act
        String orderNumber = checkoutService.finalizeOrder(userId, 0);
        
        // Assert
        assertNotNull(orderNumber);
        assertTrue(orderNumber.startsWith("ORD-"));
        
        // Verify order history entries were saved
        verify(orderHistoryRepository, times(2)).save(any(OrderHistory.class));
        
        // Verify stock was updated
        verify(itemRepository, times(2)).save(any(Item.class));
        
        // Verify cart was cleared
        verify(cartService, times(1)).clearCart(userId);
    }

    @Test
    void testFinalizeOrder_UserNotFound() {
        // Arrange
        String userId = "nonexistent";
        when(userRepository.findByUsername(userId)).thenReturn(null);
        
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> checkoutService.finalizeOrder(userId, 0)
        );
        
        assertEquals("User not found: nonexistent", exception.getMessage());
    }

    @Test
    void testFinalizeOrder_EmptyCart() {
        // Arrange
        String userId = "testuser";
        when(userRepository.findByUsername(userId)).thenReturn(testUser);
        when(cartService.isEmpty(userId)).thenReturn(true);
        
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> checkoutService.finalizeOrder(userId, 0)
        );
        
        assertEquals("Cannot checkout with empty cart", exception.getMessage());
    }
}