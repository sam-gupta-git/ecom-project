package com.revature.training.ecommerce_project.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.revature.training.ecommerce_project.services.CheckoutService;
import java.util.Map;

@RestController
@RequestMapping("/api/checkout")
@CrossOrigin(origins = "*")
public class CheckoutController {

    private final CheckoutService checkoutService;

    public CheckoutController(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    @GetMapping("/summary/{userId}")
    public ResponseEntity<Map<String, Object>> viewCheckoutPage(@PathVariable String userId) {
        try {
            Map<String, Object> summary = checkoutService.getCheckoutSummary(userId);
            return ResponseEntity.ok(summary);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/submit/{userId}")
    public ResponseEntity<Map<String, Object>> submitOrder(@PathVariable String userId, 
                                                          @RequestBody(required = false) Map<String, Object> orderData) {
        try {
            // Validate the order first
            checkoutService.validateOrder(userId);
            
            // Get checkout data ID from request body (defaulting to 0 if not provided)
            int checkoutDataID = orderData != null && orderData.containsKey("checkoutDataID") 
                ? (Integer) orderData.get("checkoutDataID") : 0;
            
            // Finalize the order and get the order number
            String orderNumber = checkoutService.finalizeOrder(userId, checkoutDataID);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Order submitted successfully",
                "orderNumber", orderNumber,
                "orderId", orderNumber // Keep for backward compatibility
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Failed to submit order"));
        }
    }

    @PostMapping("/discount/{userId}")
    public ResponseEntity<Map<String, Object>> applyDiscountCode(@PathVariable String userId, 
                                                                @RequestBody Map<String, String> request) {
        try {
            String code = request.get("code");
            if (code == null || code.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Discount code is required"));
            }
            
            checkoutService.applyDiscountCode(userId, code.trim());
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Discount code applied successfully",
                "appliedCode", checkoutService.getAppliedDiscountCode(userId),
                "discountAmount", checkoutService.getDiscountAmount(userId)
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Failed to apply discount code"));
        }
    }

    @DeleteMapping("/discount/{userId}")
    public ResponseEntity<Map<String, Object>> removeDiscountCode(@PathVariable String userId) {
        try {
            checkoutService.removeDiscountCode(userId);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Discount code removed successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Failed to remove discount code"));
        }
    }

    @PostMapping("/shipping/{userId}")
    public ResponseEntity<Map<String, Object>> selectShippingMethod(@PathVariable String userId, 
                                                                   @RequestBody Map<String, String> request) {
        try {
            String shippingMethod = request.get("shippingMethod");
            String address = request.get("address");
            
            if (shippingMethod == null || shippingMethod.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Shipping method is required"));
            }
            
            checkoutService.setShippingMethod(userId, shippingMethod, address);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Shipping method selected successfully",
                "shippingMethod", shippingMethod,
                "shippingCost", checkoutService.calculateShippingCost(userId)
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Failed to set shipping method"));
        }
    }

    @PostMapping("/payment/{userId}")
    public ResponseEntity<Map<String, Object>> selectPaymentMethod(@PathVariable String userId, 
                                                                  @RequestBody Map<String, String> request) {
        try {
            String paymentMethod = request.get("paymentMethod");
            
            if (paymentMethod == null || paymentMethod.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Payment method is required"));
            }
            
            checkoutService.setPaymentMethod(userId, paymentMethod);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Payment method selected successfully",
                "paymentMethod", paymentMethod
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Failed to set payment method"));
        }
    }

    @GetMapping("/summary/{userId}/detailed")
    public ResponseEntity<Map<String, Object>> viewCheckoutSummary(@PathVariable String userId) {
        try {
            Map<String, Object> summary = checkoutService.getCheckoutSummary(userId);
            
            // Add additional checkout-specific information
            summary.put("timestamp", System.currentTimeMillis());
            summary.put("currency", "USD");
            
            return ResponseEntity.ok(summary);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/validate/{userId}")
    public ResponseEntity<Map<String, Object>> validateCheckout(@PathVariable String userId) {
        try {
            checkoutService.validateOrder(userId);
            return ResponseEntity.ok(Map.of(
                "valid", true,
                "message", "Order is valid and ready for checkout"
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "valid", false,
                "error", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                "valid", false,
                "error", "Validation failed"
            ));
        }
    }

    @GetMapping("/total/{userId}")
    public ResponseEntity<Map<String, Object>> getOrderTotal(@PathVariable String userId) {
        try {
            double total = checkoutService.calculateTotal(userId);
            double tax = checkoutService.calculateTaxes(userId);
            double shipping = checkoutService.calculateShippingCost(userId);
            
            return ResponseEntity.ok(Map.of(
                "total", total,
                "tax", tax,
                "shipping", shipping,
                "currency", "USD"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/discounts/available")
    public ResponseEntity<Map<String, Object>> getAvailableDiscounts() {
        try {
            Map<String, Object> discounts = Map.of(
                "codes", Map.of(
                    "SAVE10", Map.of("description", "10% off your order", "discount", 0.10),
                    "SAVE20", Map.of("description", "20% off your order", "discount", 0.20),
                    "WELCOME5", Map.of("description", "5% welcome discount", "discount", 0.05),
                    "STUDENT15", Map.of("description", "15% student discount", "discount", 0.15)
                )
            );
            
            return ResponseEntity.ok(discounts);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Failed to retrieve discount codes"));
        }
    }
}
