# 🛒 E-Commerce Backend API

A robust, scalable e-commerce backend API built with Spring Boot and MySQL. This RESTful API provides comprehensive e-commerce functionality including user management, product catalog, shopping cart, wishlist, and order processing with secure authentication and data persistence.

[![Build Status](http://ec2-54-234-94-174.compute-1.amazonaws.com:8080/job/team-01-project-backend/badge/icon)](http://ec2-54-234-94-174.compute-1.amazonaws.com:8080/job/team-01-project-backend/)

## 👥 Team Members
- **Sam Gupta** - Full Stack Developer
- **Aalok Zimmerman** - Full Stack Developer

## 🚀 Features

### 🔐 Authentication & Security
- User registration and login endpoints
- Spring Security integration
- Password validation and secure storage
- Session management
- CORS configuration for frontend integration

### 👤 User Management
- User registration with email validation
- User profile management
- Secure authentication endpoints
- User session handling

### 📦 Product Management
- Product CRUD operations
- Category-based product organization
- Product search and filtering
- Inventory management
- Product image URL support

### 🛒 Shopping Cart
- Add/remove items from cart
- Quantity management
- User-specific cart persistence
- Cart validation and summary

### 💝 Wishlist Management
- Add/remove items from wishlist
- User-specific wishlist persistence
- Wishlist item tracking

### 📋 Order Processing
- Order creation and management
- Order history tracking
- Order validation and processing
- Checkout summary calculations

## 🛠️ Tech Stack

### Backend Framework
- **Spring Boot** (3.5.6) - Enterprise Java framework
- **Spring Web** - REST API development
- **Spring Data JPA** - Data persistence and ORM
- **Spring Security** - Authentication and authorization
- **Spring Boot Actuator** - Application monitoring and metrics
- **Spring Boot DevTools** - Development utilities

### Database & Persistence
- **MySQL** - Primary relational database
- **JPA/Hibernate** - Object-relational mapping
- **MySQL Connector/J** - Database connectivity

### Build & Development Tools
- **Maven** - Dependency management and build automation
- **Java 17** - Programming language
- **Lombok** - Boilerplate code reduction
- **Spring Boot Validation** - Input validation

### Deployment & Infrastructure
- **Docker** - Containerization
- **Jenkins** - CI/CD pipeline automation
- **AWS EC2** - Application hosting

## 📋 Prerequisites

Before running this application, ensure you have the following installed:

- **Java 17** or higher - [Download here](https://adoptium.net/)
- **Maven** (3.8+) - [Download here](https://maven.apache.org/download.cgi)
- **MySQL** (8.0+) - [Download here](https://dev.mysql.com/downloads/)
- **Git** - For version control

## 🚀 Getting Started

### 1. Clone the Repository
```bash
git clone https://github.com/sam-gupta-git/ecom-project.git
cd ecom-project
```

### 2. Build and Run (Default H2 Database)
```bash
# Clean and compile
./mvnw clean compile

# Run tests
./mvnw test

# Start the application
./mvnw spring-boot:run
```

The API will be available at `http://localhost:8081/api`

> **Default Configuration**: The application runs with H2 in-memory database out of the box. Data is reset on each restart.

### 3. Optional: MySQL Database Setup
If you want to use MySQL instead of H2, follow these steps:

#### 3a. Install and Configure MySQL
Create a MySQL database:
```sql
CREATE DATABASE ecommerce_db;
CREATE USER 'ecommerce_user'@'localhost' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON ecommerce_db.* TO 'ecommerce_user'@'localhost';
FLUSH PRIVILEGES;
```

#### 3b. Create Configuration File
Create `src/main/resources/application.properties`:
```properties
# Server Configuration
server.port=8081

# MySQL Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce_db
spring.datasource.username=ecommerce_user
spring.datasource.password=your_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA/Hibernate Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect

# CORS Configuration
server.servlet.context-path=/api
```

### 4. Build and Run with MySQL
```bash
# Clean and compile
./mvnw clean compile

# Run tests
./mvnw test

# Start the application
./mvnw spring-boot:run
```

The API will be available at `http://localhost:8081/api`

### 5. Verify Installation
Test the API health endpoint:
```bash
curl http://localhost:8081/api/actuator/health
```

## 📦 Available Scripts

### Maven Commands
- `./mvnw clean` - Clean build artifacts
- `./mvnw compile` - Compile the application
- `./mvnw test` - Run unit tests
- `./mvnw spring-boot:run` - Start the application
- `./mvnw package` - Create JAR file
- `./mvnw spring-boot:build-image` - Build Docker image

## 🏗️ Project Structure

```
src/
├── main/
│   └── java/
│       └── com/revature/training/ecommerce_project/
│           ├── EcommerceProjectApplication.java  # Main application class
│           ├── config/
│           │   └── DataInitializer.java         # Initial data setup
│           ├── controllers/                     # REST API endpoints
│           │   ├── CartController.java          # Shopping cart operations
│           │   ├── CheckoutController.java      # Checkout process
│           │   ├── ItemController.java          # Product management
│           │   ├── OrderHistoryController.java  # Order tracking
│           │   ├── UserController.java          # User management
│           │   └── WishlistController.java      # Wishlist operations
│           ├── model/                          # JPA entity classes
│           │   ├── User.java                   # User entity
│           │   ├── Item.java                   # Product entity
│           │   ├── Cart.java                   # Cart entity
│           │   ├── Wishlist.java               # Wishlist entity
│           │   └── OrderHistory.java           # Order entity
│           ├── repository/                     # Data access layer
│           │   └── [Repository interfaces]     # JPA repositories
│           ├── services/                       # Business logic layer
│           │   └── [Service classes]           # Application services
│           └── Utilities/                      # Helper classes
└── test/                                       # Test classes
    └── java/
        └── com/revature/training/ecommerce_project/
            └── EcommerceProjectApplicationTests.java
```

## 🔌 API Endpoints

### Authentication
- `POST /api/users/register` - User registration
- `POST /api/users/login` - User login
- `POST /api/users/logout` - User logout

### Products
- `GET /api/items` - Get all products
- `GET /api/items/{id}` - Get product by ID
- `POST /api/items` - Create new product
- `PUT /api/items/{id}` - Update product
- `DELETE /api/items/{id}` - Delete product

### Shopping Cart
- `GET /api/cart/view/{username}` - Get user cart
- `POST /api/cart/add/{username}` - Add item to cart
- `PUT /api/cart/update/{username}` - Update cart item quantity
- `DELETE /api/cart/remove/{username}` - Remove item from cart
- `DELETE /api/cart/clear/{username}` - Clear cart

### Wishlist
- `GET /api/wishlist/{username}` - Get user wishlist
- `POST /api/wishlist/add/{username}` - Add item to wishlist
- `DELETE /api/wishlist/remove/{username}` - Remove item from wishlist

### Orders
- `POST /api/orders/submit/{username}` - Create order
- `GET /api/orders/history/{username}` - Get order history
- `GET /api/checkout/summary/{username}` - Get checkout summary

## 🚀 Deployment

### Docker Deployment
1. Build the Docker image:
   ```bash
   docker build -t ecommerce-backend .
   ```

2. Run the container:
   ```bash
   docker run -p 8081:8081 -e SPRING_PROFILES_ACTIVE=production ecommerce-backend
   ```

### AWS EC2 Deployment
The application is configured for deployment on AWS EC2:
- Automated deployment via Jenkins CI/CD pipeline
- Production configuration for cloud environment
- Health checks and monitoring via Spring Actuator

### Jenkins CI/CD Pipeline
The project includes automated deployment:
- Automatic builds on code commits
- Unit test execution
- Docker image creation
- Production deployment to AWS EC2

## 🧪 Testing

Run the test suite:
```bash
./mvnw test
```

For integration tests:
```bash
./mvnw verify
```

## 🔧 Configuration

### Database Configuration
The application uses MySQL with JPA/Hibernate for data persistence. Configure database connection in `application.properties`.

### Security Configuration
Spring Security is configured for:
- CORS handling for frontend integration
- Basic authentication (can be extended to JWT)
- Secure password handling

## 🐛 Troubleshooting

### Common Issues

**Database Connection Errors:**
- Verify MySQL is running and accessible
- Check database credentials in application.properties
- Ensure database exists and user has proper permissions

**Port Conflicts:**
- Default port is 8081, change in application.properties if needed
- Check for other applications using the same port

**Build Errors:**
- Ensure Java 17 is installed and JAVA_HOME is set
- Clear Maven cache: `./mvnw dependency:purge-local-repository`
- Check Maven version compatibility

## 📚 Documentation

- **User Stories**: [Google Docs](https://docs.google.com/document/d/1kRxmpTiGa4_0o9IEfo-70xOQcrNOvkqmKTsOBRFSVxI/edit?usp=sharing)
- **Project Management**: [Trello Board](https://trello.com/invite/b/68ed4d49febe3b2b2455831e/ATTI9f5fa5090216f16be60333e3437e2ed1FA691ADB/ecommerce-project)

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is for educational purposes as part of a training program.

## 📞 Support

For questions or support, please contact:
- Aalok Zimmerman - azimmerman1245
- Sam Gupta - sam-gupta-git

