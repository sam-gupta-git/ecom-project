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

import java.util.Arrays;
import java.util.Map;

public class CheckoutServiceTest {

    @Mock
    private CartService cartService;

    @InjectMocks
    private CheckoutService checkoutService;

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
    void testCalculateTotal_WithoutDiscount() {
        // Arrange
        when(cartService.getSubtotal("user123")).thenReturn(30.00);

        // Act
        double total = checkoutService.calculateTotal("user123");

        // Assert - should include tax (8.5%) but no shipping (over $50 threshold)
        assertEquals(32.55, total, 0.01); // 30.00 + (30.00 * 0.085) = 32.55
    }

    @Test
    void testCalculateTotal_WithShipping() {
        // Arrange
        when(cartService.getSubtotal("user123")).thenReturn(25.00);

        // Act
        double total = checkoutService.calculateTotal("user123");

        // Assert - should include tax and shipping
        double expectedTotal = 25.00 + (25.00 * 0.085) + 9.99; // 37.115
        assertEquals(expectedTotal, total, 0.01);
    }

    @Test
    void testCalculateTotal_WithDiscount() {
        // Arrange
        when(cartService.getSubtotal("user123")).thenReturn(100.00);
        when(cartService.isEmpty("user123")).thenReturn(false);
        
        // Apply 10% discount
        checkoutService.applyDiscountCode("user123", "SAVE10");

        // Act
        double total = checkoutService.calculateTotal("user123");

        // Assert - 100 - 10% = 90, + 8.5% tax = 97.65, no shipping (over $50)
        assertEquals(97.65, total, 0.01);
    }

    @Test
    void testApplyDiscountCode_Valid() {
        // Arrange
        when(cartService.isEmpty("user123")).thenReturn(false);

        // Act
        checkoutService.applyDiscountCode("user123", "SAVE10");

        // Assert
        assertEquals("SAVE10", checkoutService.getAppliedDiscountCode("user123"));
    }

    @Test
    void testApplyDiscountCode_Invalid() {
        // Arrange
        when(cartService.isEmpty("user123")).thenReturn(false);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            checkoutService.applyDiscountCode("user123", "INVALID");
        });
    }

    @Test
    void testApplyDiscountCode_EmptyCart() {
        // Arrange
        when(cartService.isEmpty("user123")).thenReturn(true);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            checkoutService.applyDiscountCode("user123", "SAVE10");
        });
    }

    @Test
    void testCalculateTaxes() {
        // Arrange
        when(cartService.getSubtotal("user123")).thenReturn(100.00);

        // Act
        double tax = checkoutService.calculateTaxes("user123");

        // Assert
        assertEquals(8.50, tax, 0.01); // 100.00 * 0.085
    }

    @Test
    void testCalculateShippingCost_FreeShipping() {
        // Arrange
        when(cartService.getSubtotal("user123")).thenReturn(75.00);

        // Act
        double shipping = checkoutService.calculateShippingCost("user123");

        // Assert
        assertEquals(0.0, shipping, 0.01); // Free shipping over $50
    }

    @Test
    void testCalculateShippingCost_PaidShipping() {
        // Arrange
        when(cartService.getSubtotal("user123")).thenReturn(25.00);

        // Act
        double shipping = checkoutService.calculateShippingCost("user123");

        // Assert
        assertEquals(9.99, shipping, 0.01); // Paid shipping under $50
    }

    @Test
    void testValidateOrder_EmptyCart() {
        // Arrange
        when(cartService.isEmpty("user123")).thenReturn(true);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            checkoutService.validateOrder("user123");
        });
    }

    @Test
    void testValidateOrder_InsufficientStock() {
        // Arrange
        when(cartService.isEmpty("user123")).thenReturn(false);
        testItem.setStockQuantity(1); // Less than cart quantity (2)
        when(cartService.viewCart("user123")).thenReturn(Arrays.asList(testCartItem));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            checkoutService.validateOrder("user123");
        });
    }

    @Test
    void testGetCheckoutSummary() {
        // Arrange
        when(cartService.getSubtotal("user123")).thenReturn(100.00);
        when(cartService.getTotalItemCount("user123")).thenReturn(3);
        when(cartService.isEmpty("user123")).thenReturn(false);
        
        checkoutService.applyDiscountCode("user123", "SAVE10");

        // Act
        Map<String, Object> summary = checkoutService.getCheckoutSummary("user123");

        // Assert
        assertEquals(100.00, (Double) summary.get("subtotal"), 0.01);
        assertEquals(10.00, (Double) summary.get("discountAmount"), 0.01);
        assertEquals(7.65, (Double) summary.get("tax"), 0.01); // (100-10) * 0.085
        assertEquals(0.0, (Double) summary.get("shipping"), 0.01); // Free shipping
        assertEquals(97.65, (Double) summary.get("total"), 0.01);
        assertEquals(3, (Integer) summary.get("itemCount"));
        assertEquals("SAVE10", summary.get("appliedDiscountCode"));
        assertEquals(true, (Boolean) summary.get("freeShipping"));
    }
}