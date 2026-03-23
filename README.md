# 🛒 E-Commerce Backend — Spring Boot

A full-featured **e-commerce web application** backend built with **Spring Boot 3.5**, **Java 21**, and **PostgreSQL**. The project covers the complete shopping lifecycle: product browsing, cart/basket management, order placement, and a fully protected admin panel for managing the catalog.

---

## 📋 Table of Contents

- [Features](#-features)
- [Tech Stack](#-tech-stack)
- [Project Structure](#-project-structure)
- [Domain Model](#-domain-model)
- [Getting Started](#-getting-started)
- [Configuration](#-configuration)
- [Running the Application](#-running-the-application)
- [Admin Panel](#-admin-panel)
- [Security](#-security)
- [API / Routes Overview](#-routes-overview)
- [Contributing](#-contributing)

---

## ✨ Features

### Storefront (Customer-facing)
- **Home page** — featured products and categories
- **Shop page** — browse all products with filtering
- **Product detail page** — view product photos, sizes, colors, and stock
- **Shopping cart / basket** — add products, update quantities, remove items
- **Checkout** — place orders
- **User authentication** — register, login, logout, forgot password

### Admin Panel (`/admin/**`)
- **Dashboard** — overview of store activity
- **Category management** — create, edit, delete categories
- **Product management** — create products with multiple photos, assign categories, colors, sizes
- **Color management** — manage available colors
- **Size management** — manage available sizes
- **Color-Size stock management** — track inventory per product variant (color + size combination)

---

## 🛠 Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 3.5.7 |
| ORM | Spring Data JPA (Hibernate) |
| Database | PostgreSQL |
| Security | Spring Security |
| Templating | Thymeleaf + Thymeleaf Layout Dialect |
| Validation | Spring Bean Validation (Jakarta) |
| Messaging | Spring AMQP (RabbitMQ) |
| Mapping | ModelMapper 3.2 |
| Utilities | Lombok |
| Build Tool | Maven |

---

## 📁 Project Structure

```
src/
└── main/
    ├── java/az/edu/itbrains/ecommerce/
    │   ├── EcommerceApplication.java      # Application entry point
    │   ├── config/
    │   │   └── Config.java                # ModelMapper, RabbitMQ beans, etc.
    │   ├── controllers/
    │   │   ├── admin/                     # Admin panel controllers
    │   │   │   ├── CategoryController.java
    │   │   │   ├── ColorController.java
    │   │   │   ├── DashboardController.java
    │   │   │   ├── ProductController.java
    │   │   │   └── SizeController.java
    │   │   ├── AuthController.java        # Login, register, forgot password
    │   │   ├── BasketController.java      # Add/remove items from basket
    │   │   ├── CartController.java        # Cart view and checkout
    │   │   ├── HomeController.java        # Home page
    │   │   └── ShopController.java        # Shop listing and product detail
    │   ├── dtos/                          # Data Transfer Objects (request/response)
    │   │   ├── auth/                      # LoginDto, RegisterDto, etc.
    │   │   ├── basket/
    │   │   ├── category/
    │   │   ├── color/
    │   │   ├── colorSize/
    │   │   ├── photo/
    │   │   ├── product/
    │   │   └── size/
    │   ├── enums/                         # Enumerations (e.g., Role types)
    │   ├── exceptions/                    # Custom exception classes
    │   ├── helpers/                       # Utility/helper classes
    │   ├── models/                        # JPA Entity classes
    │   │   ├── Basket.java
    │   │   ├── Category.java
    │   │   ├── Color.java
    │   │   ├── ColorSize.java
    │   │   ├── Order.java
    │   │   ├── OrderItem.java
    │   │   ├── Photo.java
    │   │   ├── Product.java
    │   │   ├── Role.java
    │   │   ├── Size.java
    │   │   └── User.java
    │   ├── repositories/                  # Spring Data JPA repositories
    │   ├── security/
    │   │   ├── CustomUserDetailService.java
    │   │   └── SecurityConfig.java
    │   └── services/                      # Business logic layer
    │       ├── impls/                     # Service implementations
    │       ├── BasketService.java
    │       ├── CategoryService.java
    │       ├── ColorService.java
    │       ├── ColorSizeService.java
    │       ├── PhotoService.java
    │       ├── ProductService.java
    │       ├── SizeService.java
    │       └── UserService.java
    └── resources/
        ├── application.properties.example  # Configuration template
        ├── static/                          # CSS, JS, images
        └── templates/                       # Thymeleaf HTML templates
```

---

## 🗂 Domain Model

```
User ──────────── Role (ADMIN / USER)
  │
  ├── Basket ───── BasketItem ──── Product
  │
  └── Order ────── OrderItem ───── Product
                                      │
                          ┌───────────┼───────────┐
                          │           │           │
                       Photo       Category   ColorSize
                                              /       \
                                          Color       Size
```

| Entity | Description |
|---|---|
| `User` | Registered customer or admin |
| `Role` | Defines user permissions (ADMIN, USER) |
| `Product` | Items listed in the store |
| `Category` | Product grouping (e.g., Electronics, Clothing) |
| `Photo` | Multiple images per product |
| `Color` | Available color options |
| `Size` | Available size options (S, M, L, XL, etc.) |
| `ColorSize` | Variant combining Color + Size with stock quantity |
| `Basket` | A user's active shopping cart |
| `Order` | A confirmed purchase by a user |
| `OrderItem` | Individual line items within an order |

---

## 🚀 Getting Started

### Prerequisites

Make sure you have the following installed:

- [Java 21](https://adoptium.net/)
- [Apache Maven 3.8+](https://maven.apache.org/)
- [PostgreSQL 14+](https://www.postgresql.org/)
- *(Optional)* [RabbitMQ](https://www.rabbitmq.com/) — for order notification messaging

### 1. Clone the repository

```bash
git clone https://github.com/YOUR_USERNAME/YOUR_REPO_NAME.git
cd YOUR_REPO_NAME
```

### 2. Create the database

Open `psql` or pgAdmin and run:

```sql
CREATE DATABASE ecommerce_db;
```

### 3. Configure the application

Copy the example properties file and fill in your values:

```bash
cp src/main/resources/application.properties.example src/main/resources/application.properties
```

Then edit `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/ecommerce_db
spring.datasource.username=postgres
spring.datasource.password=YOUR_PASSWORD
```

> ⚠️ **Never commit** `application.properties` — it is excluded via `.gitignore`.

### 4. Build the project

```bash
./mvnw clean install
```

Or on Windows:

```cmd
mvnw.cmd clean install
```

---

## ▶️ Running the Application

```bash
./mvnw spring-boot:run
```

The application starts at: **http://localhost:8080**

| URL | Description |
|---|---|
| `http://localhost:8080/` | Storefront home page |
| `http://localhost:8080/shop` | Product listing page |
| `http://localhost:8080/auth/login` | Login page |
| `http://localhost:8080/auth/register` | Registration page |
| `http://localhost:8080/admin` | Admin dashboard (requires ADMIN role) |

---

## 🔐 Admin Panel

The admin panel is accessible at `/admin/**` and is **restricted to users with the ADMIN role**.

| Route | Description |
|---|---|
| `GET /admin` | Dashboard overview |
| `GET /admin/categories` | List all categories |
| `POST /admin/categories/add` | Create a new category |
| `GET /admin/products` | List all products |
| `POST /admin/products/add` | Add a new product with photos |
| `GET /admin/colors` | Manage colors |
| `GET /admin/sizes` | Manage sizes |

To access the admin panel, register a user and manually assign the `ADMIN` role in the database:

```sql
-- Find user id
SELECT id FROM users WHERE email = 'your@email.com';

-- Assign admin role (adjust table/column names if different)
INSERT INTO user_roles (user_id, role_id) VALUES (<user_id>, <admin_role_id>);
```

---

## 🔒 Security

Security is handled by **Spring Security** with the following setup:

- **Form-based authentication** — login at `/auth/login`
- **Role-based access control** — `ADMIN` role required for `/admin/**`
- **BCrypt password hashing** — passwords are never stored in plain text
- **CSRF protection** — enabled by default for all state-changing requests
- **Custom `UserDetailsService`** — loads users from the database by email/username

---

## 🗺 Routes Overview

### Storefront

| Method | Route | Description |
|---|---|---|
| GET | `/` | Home page |
| GET | `/shop` | All products |
| GET | `/shop/detail/{id}` | Product detail |
| GET | `/cart` | View cart |
| POST | `/basket/add` | Add item to basket |
| POST | `/basket/remove/{id}` | Remove item from basket |
| GET | `/cart/checkout` | Checkout page |
| POST | `/cart/order` | Place order |

### Authentication

| Method | Route | Description |
|---|---|---|
| GET | `/auth/login` | Login page |
| POST | `/auth/login` | Process login |
| GET | `/auth/register` | Register page |
| POST | `/auth/register` | Process registration |
| GET | `/auth/logout` | Logout |
| GET | `/auth/forgot-password` | Forgot password page |

---

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/your-feature-name`
3. Commit your changes: `git commit -m "feat: add your feature"`
4. Push to the branch: `git push origin feature/your-feature-name`
5. Open a Pull Request

---

## 📄 License

This project is for educational purposes. Feel free to use it as a learning reference.

---

*Built with ❤️ using Spring Boot*
