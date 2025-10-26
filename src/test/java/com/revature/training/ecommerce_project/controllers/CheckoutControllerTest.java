package com.revature.training.ecommerce_project.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.revature.training.ecommerce_project.services.CheckoutService;
import java.util.Map;

public class CheckoutControllerTest {

    @Mock
    private CheckoutService checkoutService;

    @InjectMocks
    private CheckoutController checkoutController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testViewCheckoutPage_Success() {
        // Arrange
        Map<String, Object> expectedSummary = Map.of(
            "subtotal", 100.0,
            "tax", 8.5,
            "shipping", 0.0,
            "total", 108.5
        );
        when(checkoutService.getCheckoutSummary("user123")).thenReturn(expectedSummary);

        // Act
        ResponseEntity<Map<String, Object>> response = checkoutController.viewCheckoutPage("user123");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedSummary, response.getBody());
    }

    @Test
    void testViewCheckoutPage_Error() {
        // Arrange
        when(checkoutService.getCheckoutSummary("user123")).thenThrow(new RuntimeException("Error"));

        // Act
        ResponseEntity<Map<String, Object>> response = checkoutController.viewCheckoutPage("user123");

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().containsKey("error"));
    }

    @Test
    void testSubmitOrder_Success() {
        // Arrange
        doNothing().when(checkoutService).validateOrder("user123");
        doNothing().when(checkoutService).finalizeOrder("user123", 0);

        // Act
        ResponseEntity<Map<String, Object>> response = checkoutController.submitOrder("user123", null);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> body = response.getBody();
        assertEquals(true, body.get("success"));
        assertEquals("Order submitted successfully", body.get("message"));
        assertTrue(body.containsKey("orderId"));
    }

    @Test
    void testSubmitOrder_ValidationError() {
        // Arrange
        doThrow(new IllegalArgumentException("Cart is empty")).when(checkoutService).validateOrder("user123");

        // Act
        ResponseEntity<Map<String, Object>> response = checkoutController.submitOrder("user123", null);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Cart is empty", response.getBody().get("error"));
    }

    @Test
    void testApplyDiscountCode_Success() {
        // Arrange
        Map<String, String> request = Map.of("code", "SAVE10");
        doNothing().when(checkoutService).applyDiscountCode("user123", "SAVE10");
        when(checkoutService.getAppliedDiscountCode("user123")).thenReturn("SAVE10");
        when(checkoutService.getDiscountAmount("user123")).thenReturn(10.0);

        // Act
        ResponseEntity<Map<String, Object>> response = checkoutController.applyDiscountCode("user123", request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> body = response.getBody();
        assertEquals(true, body.get("success"));
        assertEquals("SAVE10", body.get("appliedCode"));
        assertEquals(10.0, body.get("discountAmount"));
    }

    @Test
    void testApplyDiscountCode_InvalidCode() {
        // Arrange
        Map<String, String> request = Map.of("code", "INVALID");
        doThrow(new IllegalArgumentException("Invalid discount code")).when(checkoutService).applyDiscountCode("user123", "INVALID");

        // Act
        ResponseEntity<Map<String, Object>> response = checkoutController.applyDiscountCode("user123", request);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid discount code", response.getBody().get("error"));
    }

    @Test
    void testApplyDiscountCode_EmptyCode() {
        // Arrange
        Map<String, String> request = Map.of("code", "");

        // Act
        ResponseEntity<Map<String, Object>> response = checkoutController.applyDiscountCode("user123", request);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Discount code is required", response.getBody().get("error"));
    }

    @Test
    void testRemoveDiscountCode_Success() {
        // Arrange
        doNothing().when(checkoutService).removeDiscountCode("user123");

        // Act
        ResponseEntity<Map<String, Object>> response = checkoutController.removeDiscountCode("user123");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> body = response.getBody();
        assertEquals(true, body.get("success"));
        assertEquals("Discount code removed successfully", body.get("message"));
    }

    @Test
    void testSelectShippingMethod_Success() {
        // Arrange
        Map<String, String> request = Map.of(
            "shippingMethod", "express",
            "address", "123 Main St"
        );
        doNothing().when(checkoutService).setShippingMethod("user123", "express", "123 Main St");
        when(checkoutService.calculateShippingCost("user123")).thenReturn(15.99);

        // Act
        ResponseEntity<Map<String, Object>> response = checkoutController.selectShippingMethod("user123", request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> body = response.getBody();
        assertEquals(true, body.get("success"));
        assertEquals("express", body.get("shippingMethod"));
        assertEquals(15.99, body.get("shippingCost"));
    }

    @Test
    void testSelectPaymentMethod_Success() {
        // Arrange
        Map<String, String> request = Map.of("paymentMethod", "credit-card");
        doNothing().when(checkoutService).setPaymentMethod("user123", "credit-card");

        // Act
        ResponseEntity<Map<String, Object>> response = checkoutController.selectPaymentMethod("user123", request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> body = response.getBody();
        assertEquals(true, body.get("success"));
        assertEquals("credit-card", body.get("paymentMethod"));
    }

    @Test
    void testValidateCheckout_Valid() {
        // Arrange
        doNothing().when(checkoutService).validateOrder("user123");

        // Act
        ResponseEntity<Map<String, Object>> response = checkoutController.validateCheckout("user123");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> body = response.getBody();
        assertEquals(true, body.get("valid"));
        assertEquals("Order is valid and ready for checkout", body.get("message"));
    }

    @Test
    void testValidateCheckout_Invalid() {
        // Arrange
        doThrow(new IllegalArgumentException("Cart is empty")).when(checkoutService).validateOrder("user123");

        // Act
        ResponseEntity<Map<String, Object>> response = checkoutController.validateCheckout("user123");

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Map<String, Object> body = response.getBody();
        assertEquals(false, body.get("valid"));
        assertEquals("Cart is empty", body.get("error"));
    }

    @Test
    void testGetOrderTotal_Success() {
        // Arrange
        when(checkoutService.calculateTotal("user123")).thenReturn(108.5);
        when(checkoutService.calculateTaxes("user123")).thenReturn(8.5);
        when(checkoutService.calculateShippingCost("user123")).thenReturn(0.0);

        // Act
        ResponseEntity<Map<String, Object>> response = checkoutController.getOrderTotal("user123");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> body = response.getBody();
        assertEquals(108.5, body.get("total"));
        assertEquals(8.5, body.get("tax"));
        assertEquals(0.0, body.get("shipping"));
        assertEquals("USD", body.get("currency"));
    }

    @Test
    void testGetAvailableDiscounts_Success() {
        // Act
        ResponseEntity<Map<String, Object>> response = checkoutController.getAvailableDiscounts();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> body = response.getBody();
        assertTrue(body.containsKey("codes"));
        
        @SuppressWarnings("unchecked")
        Map<String, Object> codes = (Map<String, Object>) body.get("codes");
        assertTrue(codes.containsKey("SAVE10"));
        assertTrue(codes.containsKey("SAVE20"));
        assertTrue(codes.containsKey("WELCOME5"));
        assertTrue(codes.containsKey("STUDENT15"));
    }
}