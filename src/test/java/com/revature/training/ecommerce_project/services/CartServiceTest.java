package com.revature.training.ecommerce_project.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.revature.training.ecommerce_project.model.CartItem;
import com.revature.training.ecommerce_project.model.Item;
import com.revature.training.ecommerce_project.repository.CartRepository;
import com.revature.training.ecommerce_project.repository.ItemRepository;

import java.util.Optional;
import java.util.Arrays;
import java.util.List;

public class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private CartService cartService;

    private Item testItem;
    private CartItem testCartItem;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        testItem = new Item();
        testItem.setId(1L);
        testItem.setName("Test Item");
        testItem.setDescription("Test Description");
        testItem.setPrice(10.99);
        testItem.setStockQuantity(50);
        testItem.setCategory("Test");

        testCartItem = new CartItem("user123", testItem, 2);
    }

    @Test
    void testAddToCart_NewItem() {
        // Arrange
        when(itemRepository.findById(1L)).thenReturn(Optional.of(testItem));
        when(cartRepository.findByUserIdAndItemId("user123", 1L)).thenReturn(Optional.empty());

        // Act
        cartService.addToCart("user123", 1, 2);

        // Assert
        verify(cartRepository).save(any(CartItem.class));
    }

    @Test
    void testAddToCart_ExistingItem() {
        // Arrange
        when(itemRepository.findById(1L)).thenReturn(Optional.of(testItem));
        when(cartRepository.findByUserIdAndItemId("user123", 1L)).thenReturn(Optional.of(testCartItem));

        // Act
        cartService.addToCart("user123", 1, 1);

        // Assert
        assertEquals(3, testCartItem.getQuantity());
        verify(cartRepository).save(testCartItem);
    }

    @Test
    void testAddToCart_InsufficientStock() {
        // Arrange
        testItem.setStockQuantity(1);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(testItem));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            cartService.addToCart("user123", 1, 5);
        });
    }

    @Test
    void testGetSubtotal() {
        // Arrange
        List<CartItem> cartItems = Arrays.asList(
            new CartItem("user123", testItem, 2),
            new CartItem("user123", testItem, 1)
        );
        when(cartRepository.findByUserId("user123")).thenReturn(cartItems);

        // Act
        double subtotal = cartService.getSubtotal("user123");

        // Assert
        assertEquals(32.97, subtotal, 0.01); // (10.99 * 2) + (10.99 * 1)
    }

    @Test
    void testApplyDiscountCode_Valid() {
        // Arrange
        when(cartRepository.existsByUserId("user123")).thenReturn(true);

        // Act
        cartService.applyDiscountCode("user123", "SAVE10");

        // Assert
        assertNotNull(cartService.getAppliedDiscountCode("user123"));
    }

    @Test
    void testApplyDiscountCode_Invalid() {
        // Arrange
        when(cartRepository.existsByUserId("user123")).thenReturn(true);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            cartService.applyDiscountCode("user123", "INVALID");
        });
    }

    @Test
    void testIsEmpty() {
        // Arrange
        when(cartRepository.existsByUserId("user123")).thenReturn(false);

        // Act
        boolean isEmpty = cartService.isEmpty("user123");

        // Assert
        assertTrue(isEmpty);
    }
}