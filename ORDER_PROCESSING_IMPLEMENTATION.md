# Order Processing Implementation

## Overview
This implementation adds the functionality to move items from the cart to order history when an order is placed in the e-commerce application.

## Changes Made

### 1. Updated OrderHistory Model
- **File**: `src/main/java/com/revature/training/ecommerce_project/model/OrderHistory.java`
- **Changes**:
  - Added relationship to `Item` entity to store individual order items
  - Added fields for quantity, item price, and total item price
  - Modified to store individual line items rather than just order summaries
  - Each order item is stored as a separate record with the same order number

### 2. Created OrderHistoryRepository
- **File**: `src/main/java/com/revature/training/ecommerce_project/repository/OrderHistoryRepository.java`
- **Features**:
  - Find order history by user
  - Find order history by order number
  - Get distinct order numbers for grouping
  - Order results by date (newest first)

### 3. Enhanced CheckoutService
- **File**: `src/main/java/com/revature/training/ecommerce_project/services/CheckoutService.java`
- **Updated `finalizeOrder()` method**:
  - Validates order and user existence
  - Generates unique order number (format: ORD-XXXXXXXX)
  - Creates OrderHistory entries for each cart item
  - Updates item stock quantities
  - Clears the user's cart after successful order placement
  - Returns the generated order number

### 4. Created OrderHistoryService
- **File**: `src/main/java/com/revature/training/ecommerce_project/services/OrderHistoryService.java`
- **Features**:
  - Get order history by username or user ID
  - Get order details by order number
  - Get order summaries (grouped by order number)
  - Calculate total amount spent by user

### 5. Created OrderHistoryController
- **File**: `src/main/java/com/revature/training/ecommerce_project/controllers/OrderHistoryController.java`
- **API Endpoints**:
  - `GET /api/orders/user/{username}` - Get all order history for a user
  - `GET /api/orders/user/{username}/summaries` - Get order summaries (grouped)
  - `GET /api/orders/order/{orderNumber}` - Get details for a specific order
  - `GET /api/orders/user/{username}/order/{orderNumber}` - Get specific order for user
  - `GET /api/orders/user/{username}/total-spent` - Get total amount spent by user

### 6. Updated CheckoutController
- **File**: `src/main/java/com/revature/training/ecommerce_project/controllers/CheckoutController.java`
- **Changes**:
  - Modified `submitOrder()` endpoint to return the actual order number
  - Provides both `orderNumber` and `orderId` for backward compatibility

### 7. Added Tests
- **Files**: 
  - `src/test/java/com/revature/training/ecommerce_project/services/CheckoutServiceOrderTest.java`
  - `src/test/java/com/revature/training/ecommerce_project/services/OrderHistoryServiceTest.java`
- **Coverage**: Tests for successful order placement, error handling, and order history retrieval

## Order Processing Flow

1. **Order Submission**: Frontend calls `POST /api/checkout/submit/{userId}`
2. **Validation**: System validates cart is not empty and user exists
3. **Order Creation**: 
   - Generate unique order number
   - Calculate total amount including taxes and shipping
   - Create timestamp for order
4. **Item Processing**: For each item in cart:
   - Create OrderHistory record with item details
   - Update item stock quantity
   - Save changes to database
5. **Cart Cleanup**: Clear all items from user's cart
6. **Response**: Return success message with order number

## Database Schema Changes

The `order_history` table now includes:
- `id` - Primary key
- `user_id` - Foreign key to users table
- `item_id` - Foreign key to items table
- `order_number` - Unique identifier for grouping order items
- `order_date` - Timestamp of order placement
- `quantity` - Number of items ordered
- `item_price` - Price of item at time of purchase
- `total_item_price` - item_price * quantity
- `order_total_amount` - Total amount for entire order (for reference)

## API Usage Examples

### Place an Order
```http
POST /api/checkout/submit/john_doe
Content-Type: application/json

{
  "checkoutDataID": 0
}
```

Response:
```json
{
  "success": true,
  "message": "Order submitted successfully",
  "orderNumber": "ORD-A1B2C3D4",
  "orderId": "ORD-A1B2C3D4"
}
```

### Get User's Order History
```http
GET /api/orders/user/john_doe
```

### Get Order Summaries
```http
GET /api/orders/user/john_doe/summaries
```

### Get Specific Order Details
```http
GET /api/orders/order/ORD-A1B2C3D4
```

## Key Features

1. **Transactional Safety**: All operations are wrapped in transactions
2. **Stock Management**: Automatically updates item stock quantities
3. **Order Tracking**: Unique order numbers for easy tracking
4. **Price History**: Preserves item prices at time of purchase
5. **Cart Management**: Automatically clears cart after successful order
6. **Error Handling**: Comprehensive validation and error messages
7. **REST API**: Full RESTful API for frontend integration