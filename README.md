<div align="center">

# 🛒 ShopHub — Multi-Vendor E-Commerce Platform

**A production-grade marketplace where sellers list their products, buyers place orders, and promotions drive visibility — all secured, tested, and containerized.**

[![Java](https://img.shields.io/badge/Java-21-orange?style=flat-square&logo=openjdk)](https://openjdk.org/projects/jdk/21/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.7-brightgreen?style=flat-square&logo=springboot)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue?style=flat-square&logo=postgresql)](https://www.postgresql.org/)
[![Docker](https://img.shields.io/badge/Docker-ready-2496ED?style=flat-square&logo=docker)](https://www.docker.com/)
[![CI/CD](https://img.shields.io/badge/CI%2FCD-GitHub%20Actions-2088FF?style=flat-square&logo=githubactions)](https://github.com/features/actions)
[![License](https://img.shields.io/badge/License-MIT-yellow?style=flat-square)](LICENSE)

[Live Demo](#) · [Quick Start](#-quick-start-with-docker) · [API Docs](#-routes-reference) · [Report Bug](https://github.com/AbiyevNurlan/ecommerce-backend/issues)

</div>

---

## What is ShopHub?

ShopHub is a fully-featured **multi-vendor e-commerce platform** built on Java 21 and Spring Boot 3.5. It gives you a complete marketplace out of the box:

- **Buyers** browse a rich product catalog with category, color, and size filters, add items to their cart, and checkout in seconds.
- **Sellers** apply for a storefront, list their products, and pay to boost their listings through a flexible promotion system.
- **Admins** manage the entire platform — categories, products, orders, seller approvals, and seller balances — from a sleek dashboard.

No third-party marketplace fees. No vendor lock-in. Your platform, your rules.

---

## Why ShopHub?

| Pain Point | How ShopHub Solves It |
|---|---|
| Building a marketplace from scratch takes months | Fully working multi-vendor system, ready to deploy today |
| Promotion/advertising systems are complex | Built-in balance-based promotion engine with 3 types × 3 durations |
| Managing sellers manually is a headache | Structured seller application → admin approval workflow |
| Security is an afterthought | BCrypt passwords, CSRF protection, role-based access on every route |
| "Works on my machine" syndrome | Docker Compose spins up the full stack (app + PostgreSQL) in one command |
| Untested code breaks in production | JUnit 5 unit tests + MockMvc integration tests + automated CI pipeline |

---

## Core Features

### For Buyers
- **Rich product catalog** — filter by category, color, and size simultaneously
- **Smart shopping cart** — add, update quantity, or remove items; cart persists per user
- **Seamless checkout** — one-page checkout with address entry; instant order confirmation
- **Order tracking** — real-time order status from `PENDING` through `DELIVERED`
- **User accounts** — secure registration, login, and forgot-password flow

### For Sellers
- **Seller storefront** — apply with a shop name and description; admin approves within minutes
- **Product management** — list products with images, pricing, discounts, and barcode; every new product goes through admin review before going live
- **Promotion engine** — pay with your balance to feature products in prime spots:

  | Promotion Type | Placement | 3 Days | 7 Days | 30 Days |
  |---|---|---|---|---|
  | **FEATURED** | Homepage "Featured Products" section | $10 | $20 | $60 |
  | **SPONSORED** | Top of category listing with "Sponsored" badge | $5 | $10 | $30 |
  | **HOT TRENDING** | "Trending Now" section — high-traffic real estate | $7 | $15 | $45 |

- **Balance & transaction history** — every debit and credit is recorded; full audit trail
- **Revenue analytics** — dashboard shows total products, active promotions, order count, and total revenue

### For Admins
- **Full platform control** — manage categories, products, colors, sizes, and orders
- **Seller management** — approve or reject seller applications with one click; credit seller balances directly
- **Order pipeline** — update order status across the full lifecycle
- **Commission configuration** — set individual commission rates per seller

---

## Tech Stack

| Layer | Technology |
|---|---|
| **Language** | Java 21 |
| **Framework** | Spring Boot 3.5.7 — MVC, Data JPA, Security, Scheduling |
| **View Layer** | Thymeleaf 3 + Layout Dialect + Spring Security extras |
| **Database** | PostgreSQL 16 with Flyway versioned migrations (V1–V4) |
| **ORM** | Hibernate 6 via Spring Data JPA |
| **Security** | Spring Security 6 — BCrypt, CSRF, session fixation protection, role-based access |
| **Testing** | JUnit 5, Mockito, MockMvc, H2 in-memory (integration tests) |
| **Logging** | Logback with `logback-spring.xml` — colored console + rolling file output |
| **Build** | Maven 3.9 + Maven Wrapper (no local Maven install needed) |
| **Containerization** | Docker multi-stage build, Docker Compose |
| **CI/CD** | GitHub Actions — build, test, Docker image build, OWASP security scan |
| **Utilities** | Lombok, ModelMapper 3.2, Hikari connection pool |

---

## Architecture

ShopHub follows a clean **Layered MVC** architecture with strict separation of concerns:

```
Browser ──► Controller ──► Service ──► Repository ──► PostgreSQL
                │
            Thymeleaf (Server-side rendered HTML)
```

```
src/main/java/az/edu/itbrains/ecommerce/
├── controllers/
│   ├── AuthController          # /register, /login, /forgot-password
│   ├── HomeController          # / (homepage — featured & trending products)
│   ├── ShopController          # /shop, /shop/detail/{seoUrl}
│   ├── BasketController        # /basket/add, /basket/remove/{id}
│   ├── CartController          # /cart, /cart/checkout, /cart/order
│   ├── OrderController         # /orders/my-orders
│   ├── seller/
│   │   └── SellerController    # /seller/** (dashboard, products, promotions, balance)
│   └── admin/
│       ├── DashboardController
│       ├── CategoryController
│       ├── ProductController
│       ├── ColorController
│       ├── SizeController
│       ├── AdminOrderController
│       └── AdminSellerController  # /dashboard/sellers/**
├── dtos/
│   ├── product/                # ProductCreateDto, ProductUpdateDto, ProductDto
│   └── seller/                 # SellerApplyDto, SellerDashboardDto, SellerAdminDto,
│                               # PromotionCreateDto, PromotionDto,
│                               # SellerTransactionDto, BalanceCreditDto
├── enums/
│   ├── OrderStatus             # PENDING → CONFIRMED → SHIPPED → DELIVERED → CANCELLED
│   ├── ProductStatus           # DRAFT → PENDING_REVIEW → ACTIVE / REJECTED / SUSPENDED
│   ├── PromotionType           # FEATURED | SPONSORED | HOT_TRENDING
│   ├── PromotionStatus         # PENDING → ACTIVE → EXPIRED / CANCELLED
│   └── TransactionType         # CREDIT | PROMO_DEBIT | COMMISSION_DEBIT
├── helpers/
│   ├── DataSeeder              # Seeds admin user + demo data on first startup
│   └── PromotionExpiryScheduler  # @Scheduled — expires outdated promotions hourly
├── models/                     # 14 JPA entities
├── repositories/               # Spring Data JPA interfaces
├── security/                   # SecurityConfig, CustomUserDetailsService
└── services/                   # Business logic
    ├── SellerService
    ├── PromotionService
    └── impls/
```

---

## Data Model

| Entity | Description |
|---|---|
| `User` | Platform user — name, email, BCrypt password, roles |
| `Role` | `ROLE_ADMIN` / `ROLE_USER` / `ROLE_SELLER` |
| `Product` | Product — name, price, discount, barcode, category, photos, seller, status |
| `Category` | Category with SEO-friendly URL slug |
| `Color` | Color option |
| `Size` | Size option |
| `ColorSize` | Product × Color × Size inventory table (stock per combination) |
| `Photo` | Product photos (one marked as primary) |
| `Basket` | Shopping cart line — user, product, quantity |
| `Order` | Order — shipping address, status, line items |
| `OrderItem` | Order line — product snapshot, quantity, price at purchase time |
| `Seller` | Seller profile — shop name, balance, commission rate, approval status |
| `Promotion` | Product promotion — type, duration, amount paid, status |
| `SellerTransaction` | Balance audit log — every credit, promo debit, and commission debit |

---

## Three-Role System

```
┌─────────────┐    /register      ┌─────────────┐
│   Visitor   │ ──────────────►  │  ROLE_USER  │
└─────────────┘                   └──────┬──────┘
                                         │ /seller/apply
                                         ▼
                                   ┌─────────────┐
                                   │   PENDING   │ ◄── Admin reviews
                                   └──────┬──────┘
                              Approve ▼         ▼ Reject
                                ┌───────────┐  (stays ROLE_USER)
                                │ROLE_SELLER│
                                └───────────┘
                                     │
                    Automatic redirect to /seller/dashboard after login
```

**Admin** (`ROLE_ADMIN`) — Full platform access via `/dashboard/**`  
**Seller** (`ROLE_SELLER`) — Storefront management via `/seller/**`  
**User** (`ROLE_USER`) — Shopping, cart, orders  

---

## Quick Start with Docker

The fastest way to run ShopHub locally — no Java, no PostgreSQL install needed:

```bash
# 1. Clone the repository
git clone https://github.com/AbiyevNurlan/ecommerce-backend.git
cd ecommerce-backend

# 2. Configure environment
cp .env.example .env
# Open .env and set DB_PASSWORD

# 3. Launch everything
docker-compose up --build -d

# 4. Watch the startup logs
docker-compose logs -f app
```

Open **http://localhost:8080** — the platform is live.

**To stop:**
```bash
docker-compose down          # stop containers
docker-compose down -v       # stop + wipe the database
```

---

## Local Development (Without Docker)

### Prerequisites

| Tool | Version |
|---|---|
| Java (JDK) | 21+ |
| Maven | 3.9+ (or use the included `mvnw`) |
| PostgreSQL | 16 |

### Setup

**1. Create the database:**
```sql
CREATE DATABASE ecommerce_db;
```

**2. Configure the application:**
```bash
cp src/main/resources/application.properties.example \
   src/main/resources/application.properties
```

Edit `application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/ecommerce_db
spring.datasource.username=postgres
spring.datasource.password=YOUR_PASSWORD
```

> `application.properties` is in `.gitignore` — your credentials are never committed.

**3. Run:**
```bash
# Windows
mvnw.cmd spring-boot:run

# Linux / macOS
./mvnw spring-boot:run
```

Flyway automatically runs all migrations on startup. The `DataSeeder` creates the admin account and demo data on first run.

---

## Default Credentials

| Role | Email | Password | Notes |
|---|---|---|---|
| **Admin** | `admin@admin.com` | `Admin@123` | Created by DataSeeder on first startup |
| **User** | — | — | Self-register at `/register` |
| **Seller** | — | — | Register → apply at `/seller/apply` → await admin approval |

> Change the admin password before going to production.

---

## Routes Reference

### Public Storefront

| Method | Route | Description |
|---|---|---|
| GET | `/` | Homepage — featured and trending products |
| GET | `/shop` | Full catalog with filters (category, color, size) |
| GET | `/shop/detail/{seoUrl}` | Product detail page |
| GET | `/login` | Login page |
| GET | `/register` | Registration page |
| GET | `/forgot-password` | Password reset page |

### Authenticated Buyer Routes

| Method | Route | Description |
|---|---|---|
| POST | `/basket/add` | Add product to cart |
| POST | `/basket/remove/{id}` | Remove product from cart |
| GET | `/cart` | View cart |
| GET | `/cart/checkout` | Checkout page |
| POST | `/cart/order` | Place order |
| GET | `/orders/my-orders` | Order history |

### Seller Panel (`/seller/**` — requires `ROLE_SELLER`)

| Method | Route | Description |
|---|---|---|
| GET | `/seller/apply` | Apply to become a seller (open to all users) |
| POST | `/seller/apply` | Submit seller application |
| GET | `/seller/dashboard` | Dashboard — stats, balance, revenue |
| GET | `/seller/products` | My product listings |
| GET | `/seller/products/create` | New product form |
| POST | `/seller/products/create` | Submit product (enters PENDING_REVIEW) |
| POST | `/seller/products/delete/{id}` | Delete own product |
| GET | `/seller/promotions` | Active promotions + price table |
| POST | `/seller/promotions/buy` | Purchase a promotion (deducted from balance) |
| POST | `/seller/promotions/cancel/{id}` | Cancel an active promotion |
| GET | `/seller/balance` | Balance & full transaction ledger |

### Admin Panel (`/dashboard/**` — requires `ROLE_ADMIN`)

| Method | Route | Description |
|---|---|---|
| GET | `/dashboard` | Admin overview |
| GET/POST | `/dashboard/category/**` | Category CRUD |
| GET/POST | `/dashboard/product/**` | Product CRUD |
| GET/POST | `/dashboard/color/**` | Color CRUD |
| GET/POST | `/dashboard/size/**` | Size CRUD |
| GET | `/dashboard/order` | All orders |
| POST | `/dashboard/order/status/{id}` | Update order status |
| GET | `/dashboard/sellers` | Seller applications list |
| POST | `/dashboard/sellers/approve/{id}` | Approve seller (grants ROLE_SELLER) |
| POST | `/dashboard/sellers/reject/{id}` | Reject seller |
| POST | `/dashboard/sellers/credit` | Add balance to seller account |

---

## Environment Variables

| Variable | Example | Description |
|---|---|---|
| `DB_NAME` | `ecommerce_db` | PostgreSQL database name |
| `DB_USERNAME` | `postgres` | Database user |
| `DB_PASSWORD` | `your_secure_password` | Database password — always change this |
| `DB_HOST` | `db` | Host (`db` in Docker, `localhost` for local dev) |
| `DB_PORT` | `5432` | PostgreSQL port |
| `SPRING_PROFILES_ACTIVE` | `prod` | Active profile: `dev`, `prod`, or `test` |
| `UPLOAD_DIR` | `/app/uploads` | Directory for uploaded files |

See `.env.example` for the full annotated template.

---

## Testing

ShopHub has two levels of test coverage:

### Unit Tests — Pure Business Logic

Runs with Mockito only. Zero database involvement. Fast.

| Test Class | Coverage |
|---|---|
| `UserServiceImplTest` | Registration — new email, duplicate email, role not found |
| `BasketServiceImplTest` | Add to cart, increment quantity, remove item |
| `OrderServiceImplTest` | Place order, empty cart guard, status transitions |
| `ProductServiceImplTest` | Create, update, delete, not-found handling |

### Integration Tests — Full Stack

`@SpringBootTest` + `MockMvc` + H2 in-memory database. Tests the entire request→response cycle.

| Test Class | Coverage |
|---|---|
| `AuthControllerIntegrationTest` | Login, registration, CSRF protection, validation errors |
| `ProductControllerIntegrationTest` | Admin CRUD, unauthorized access (403 / redirect) |

### Running Tests

```bash
# All tests
./mvnw test                        # Linux/macOS
mvnw.cmd test                      # Windows

# Unit tests only
mvnw.cmd test -Dtest="*ServiceImpl*"

# Generate coverage report → target/site/jacoco/index.html
mvnw.cmd verify
```

---

## CI/CD Pipeline

Every push triggers the GitHub Actions pipeline defined in `.github/workflows/ci.yml`:

| Job | Trigger | What It Does |
|---|---|---|
| **build-and-test** | Every push & PR | Maven build + full test suite against a real PostgreSQL 16 service container. Surefire XML and JaCoCo coverage uploaded as artifacts. |
| **docker-build** | Push to `main` | Multi-stage Docker image built and tagged with commit SHA + `latest`. |
| **security-scan** | Push to `main` | OWASP Dependency-Check — build fails if any dependency has CVSS score ≥ 9. HTML/XML report saved as artifact. |

---

## Database Migrations

| File | Contents |
|---|---|
| `V1__init.sql` | Full schema — all tables, foreign keys, indexes, seed roles |
| `V2__fix_order_status_column_type.sql` | Fixed `order_status` column type |
| `V3__fix_demo_product_photo.sql` | Fixed demo product image URL |
| `V4__add_seller_promotion.sql` | `sellers`, `promotions`, `seller_transactions` tables; adds `seller_id` and `product_status` to `product`; inserts `ROLE_SELLER` |

Flyway validates and applies migrations automatically on every startup. No manual SQL scripts to run.

---

## Security Highlights

- **BCrypt password hashing** — industry standard, salted, cost-factor configurable
- **CSRF protection** on all state-changing forms (Thymeleaf CSRF token helper included)
- **Session fixation protection** — new session ID issued after successful login
- **Role-based route security** — enforced at both `SecurityConfig` and `@PreAuthorize` levels
- **Input validation** — `@Valid` + Bean Validation on all form-bound DTOs
- **OWASP Dependency-Check** — catches vulnerable dependencies before they reach production
- **Non-root Docker user** — container runs as a dedicated non-root user

---

## Roadmap

| Feature | Status |
|---|---|
| Payment gateway integration (Stripe / PayPal) | Planned |
| Email notifications (order confirmation, password reset) | Planned |
| REST JSON API for mobile clients | Planned |
| Elasticsearch full-text product search | Planned |
| Redis caching for catalog pages | Planned |
| Seller analytics dashboard with charts | Planned |

---

## Contributing

Contributions are welcome.

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/your-feature`
3. Commit your changes: `git commit -m "feat: describe your change"`
4. Push the branch: `git push origin feature/your-feature`
5. Open a Pull Request against `main`

Please follow the existing code style and ensure all tests pass before opening a PR.

---

## License

Distributed under the MIT License. See `LICENSE` for details.

---

<div align="center">

Built with Java 21 · Spring Boot 3.5 · PostgreSQL 16 · Docker  
**[Back to top](#-shophub--multi-vendor-e-commerce-platform)**

</div>
