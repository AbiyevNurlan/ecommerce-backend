# ITBrains E-Commerce Platform

Spring Boot 3.5 + Java 21 əsaslı tam funksional e-ticarət platforması. Məhsul kataloqu, səbət idarəetməsi, sifariş sistemi, admin panel, Spring Security, Flyway migration, Docker, CI/CD pipeline daxildir.

---

## 📋 Mündəricat

- [Tech Stack](#-tech-stack)
- [Arxitektura](#-arxitektura)
- [Domain Modelləri](#-domain-modelləri)
- [Tələblər](#-tələblər)
- [Sürətli Başlama (Docker ilə)](#-sürətli-başlama-docker-ilə)
- [Local Başlama (Docker olmadan)](#-local-başlama-docker-olmadan)
- [Environment Variables](#-environment-variables)
- [Default Giriş Məlumatları](#-default-giriş-məlumatları)
- [API / Routes Overview](#-routes-overview)
- [Test Yazısı](#-test-yazısı)
- [CI/CD](#-cicd)
- [Layihənin Xüsusiyyətləri](#-layihənin-xüsusiyyətləri)
- [Contributing](#-contributing)

---

## 🛠 Tech Stack

| Sahə | Texnologiya |
|---|---|
| **Backend** | Java 21, Spring Boot 3.5.7, Spring MVC, Spring Data JPA (Hibernate) |
| **Frontend** | Thymeleaf 3 + Layout Dialect, Bootstrap 4, jQuery |
| **Database** | PostgreSQL 16, Flyway (versiyalanmış migration) |
| **Security** | Spring Security 6, BCrypt, CSRF qoruması, Role-based access (ADMIN / USER) |
| **Messaging** | Spring AMQP (RabbitMQ) |
| **Testing** | JUnit 5, Mockito, MockMvc, H2 (in-memory, integration tests üçün) |
| **Logging** | Logback (logback-spring.xml), SLF4J |
| **Build** | Maven 3.9, Maven Wrapper |
| **DevOps** | Docker (multi-stage), Docker Compose, GitHub Actions CI/CD |
| **Code Quality** | OWASP Dependency-Check (CVSS ≥ 9 build-i dayandırır), JaCoCo coverage |
| **Utilities** | Lombok, ModelMapper 3.2, Flyway |

---

## 🏗 Arxitektura

Layihə **Layered MVC (Model–View–Controller)** arxitekturası ilə qurulub:

```
Browser ──► Controller ──► Service ──► Repository ──► PostgreSQL
                │
            Thymeleaf (Server-side rendered HTML)
```

```
src/main/java/az/edu/itbrains/ecommerce/
├── config/               # ModelMapper, RabbitMQ, Security konfiqurasiyaları
├── controllers/          # Storefront HTTP handler-ləri
│   ├── AuthController     # /register, /login, /forgot-password
│   ├── HomeController     # / (ana səhifə)
│   ├── ShopController     # /shop, /shop/detail/{seoUrl}
│   ├── BasketController   # /basket/add, /basket/remove/{id}
│   ├── CartController     # /cart, /cart/checkout, /cart/order
│   ├── OrderController    # /orders/my-orders
│   └── admin/            # Admin panel controller-ləri (/dashboard/**)
│       ├── DashboardController
│       ├── CategoryController
│       ├── ProductController
│       ├── ColorController
│       ├── SizeController
│       └── AdminOrderController
├── dtos/                 # Data Transfer Objects (request/response)
├── enums/                # OrderStatus enum
├── exceptions/           # GlobalExceptionHandler, ResourceNotFoundException, ServiceException
├── helpers/              # DataSeeder (ilk açılışda demo data)
├── models/               # JPA Entity-lər (11 entity)
├── repositories/         # Spring Data JPA repository interface-ləri
├── security/             # SecurityConfig, CustomUserDetailsService
└── services/             # Business logic (interface + impls/)
    └── impls/
```

---

## 🗄 Domain Modelləri

| Entity | Açıqlama |
|---|---|
| `User` | İstifadəçi — ad, soyad, email, şifrə (BCrypt), rollar |
| `Role` | ROLE_ADMIN / ROLE_USER |
| `Product` | Məhsul — ad, qiymət, endirim, barkod, kateqoriya, şəkillər |
| `Category` | Kateqoriya — ad, SEO URL |
| `Color` | Rəng |
| `Size` | Ölçü |
| `ColorSize` | Məhsul–Rəng–Ölçü əlaqəsi (stok cədvəli) |
| `Photo` | Məhsul şəkilləri (selected = əsas şəkil) |
| `Basket` | Səbət sətiri — istifadəçi, məhsul, miqdar |
| `Order` | Sifariş — ünvan, status, sifariş elementləri |
| `OrderItem` | Sifariş sətiri — məhsul, miqdar, qiymət |

**Sifariş statusları:** `PENDING → CONFIRMED → SHIPPED → DELIVERED → CANCELLED`

---

## 📌 Tələblər

| Alət | Versiya |
|---|---|
| Java (JDK) | 21+ |
| Maven | 3.9+ (və ya daxili `mvnw`) |
| Docker & Docker Compose | Ən son versiya |
| PostgreSQL | 16 (yalnız local inkişaf — Docker-da avtomatik qalxır) |

---

## 🐳 Sürətli Başlama (Docker ilə)

```bash
# 1. Repo-nu klonlayın
git clone https://github.com/AbiyevNurlan/ecommerce-backend.git
cd ecommerce-backend

# 2. Environment faylını yaradın
cp .env.example .env
# .env faylını açın və DB_PASSWORD-u dəyişin

# 3. Docker Compose ilə işə salın
docker-compose up --build -d

# 4. Logları izləyin
docker-compose logs -f app
```

Tətbiq **http://localhost:8080** ünvanında qalxacaq.

**Admin ilkin giriş (DataSeeder tərəfindən avtomatik yaradılır):**

| Alan | Dəyər |
|---|---|
| Email | `admin@admin.com` |
| Şifrə | `Admin@123` |

**Dayandırmaq:**

```bash
docker-compose down        # Konteynerləri dayandır
docker-compose down -v     # + verilənlər bazası data-sını sil
```

---

## 💻 Local Başlama (Docker olmadan)

### 1. PostgreSQL-i qurun və verilənlər bazası yaradın

```sql
CREATE DATABASE ecommerce_db;
```

### 2. Application properties-i konfiqurasiya edin

```bash
cp src/main/resources/application.properties.example src/main/resources/application.properties
```

`application.properties` faylında dəyişdirin:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/ecommerce_db
spring.datasource.username=postgres
spring.datasource.password=YOUR_PASSWORD
```

> ⚠️ `application.properties` faylı `.gitignore`-dadır — GitHub-a push olunmur!

### 3. Tətbiqi işə salın

```bash
# Windows
mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=dev

# Linux / macOS
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

Tətbiq **http://localhost:8080** ünvanında başlayacaq.
Flyway avtomatik olaraq `db/migration/` qovluğundakı SQL skriptlərini icra edəcək.

---

## 🔑 Environment Variables

| Dəyişən | Nümunə | Açıqlama |
|---|---|---|
| `DB_NAME` | `ecommerce_db` | PostgreSQL verilənlər bazasının adı |
| `DB_USERNAME` | `postgres` | DB istifadəçi adı |
| `DB_PASSWORD` | `your_password_here` | DB şifrəsi — mütləq dəyişin! |
| `DB_HOST` | `db` | DB host (Docker-da `db`, local-da `localhost`) |
| `DB_PORT` | `5432` | PostgreSQL portu |
| `SPRING_PROFILES_ACTIVE` | `prod` | Aktiv profil: `dev`, `prod`, `test` |
| `UPLOAD_DIR` | `/app/uploads` | Yüklənən faylların saxlanılacağı qovluq |

> `.env.example` faylında bütün dəyişənlər şərhlərlə izah olunub.

---

## 🔐 Default Giriş Məlumatları

Tətbiq ilk dəfə başladıqda `DataSeeder` avtomatik olaraq aşağıdakıları yaradır:

| Rol | Email | Şifrə | Qeyd |
|---|---|---|---|
| **ADMIN** | `admin@admin.com` | `Admin@123` | DataSeeder tərəfindən avtomatik |
| **USER** | — | — | `/register` səhifəsindən qeydiyyat |

> ⚠️ **İstehsalda** admin şifrəsini mütləq dəyişin!

---

## 🗺 Routes Overview

### Storefront (Public)

| Method | Route | Açıqlama |
|---|---|---|
| GET | `/` | Ana səhifə (featured & hot-trending məhsullar) |
| GET | `/shop` | Bütün məhsullar (kateqoriya, rəng, ölçü filtrləri) |
| GET | `/shop/detail/{seoUrl}` | Məhsul detal səhifəsi |
| GET | `/forgot-password` | Şifrəni unutdum |

### Authentication

| Method | Route | Açıqlama |
|---|---|---|
| GET | `/login` | Giriş səhifəsi |
| POST | `/login` | Spring Security form login |
| GET | `/register` | Qeydiyyat səhifəsi |
| POST | `/register` | Yeni istifadəçi yarat |
| GET | `/logout` | Çıxış (POST Spring Security) |

### Authenticated User Routes

| Method | Route | Açıqlama |
|---|---|---|
| GET | `/cart` | Səbət səhifəsi |
| POST | `/basket/add` | Səbətə məhsul əlavə et |
| POST | `/basket/remove/{productId}` | Səbətdən məhsul sil |
| GET | `/cart/checkout` | Checkout səhifəsi |
| POST | `/cart/order` | Sifariş ver |
| GET | `/orders/my-orders` | İstifadəçinin sifarişləri |

### Admin Panel (`/dashboard/**` — ADMIN rolu tələb olunur)

| Method | Route | Açıqlama |
|---|---|---|
| GET | `/dashboard` | Admin dashboard |
| GET | `/dashboard/category` | Kateqoriya siyahısı |
| GET | `/dashboard/category/create` | Yeni kateqoriya formu |
| POST | `/dashboard/category/create` | Kateqoriya yarat |
| GET | `/dashboard/category/update/{id}` | Kateqoriya yenilə formu |
| POST | `/dashboard/category/update/{id}` | Kateqoriyani yenilə |
| POST | `/dashboard/category/delete/{id}` | Kateqoriyani sil |
| GET | `/dashboard/product` | Məhsul siyahısı |
| GET | `/dashboard/product/create` | Yeni məhsul formu |
| POST | `/dashboard/product/create` | Məhsul yarat |
| GET | `/dashboard/product/update/{id}` | Məhsul yenilə formu |
| POST | `/dashboard/product/update/{id}` | Məhsulu yenilə |
| POST | `/dashboard/product/delete/{id}` | Məhsulu sil |
| GET | `/dashboard/color` | Rəng siyahısı |
| POST | `/dashboard/color/create` | Rəng yarat |
| POST | `/dashboard/color/delete/{id}` | Rəngi sil |
| GET | `/dashboard/size` | Ölçü siyahısı |
| POST | `/dashboard/size/create` | Ölçü yarat |
| POST | `/dashboard/size/delete/{id}` | Ölçüyü sil |
| GET | `/dashboard/order` | Sifariş siyahısı |
| POST | `/dashboard/order/status/{id}` | Sifariş statusunu yenilə |

---

## 🧪 Test Yazısı

Layihədə iki səviyyəli test əhatəsi var:

### Unit Testlər (`src/test/.../services/`)

Xalis Mockito ilə yalnız service logikasını test edir — verilənlər bazası iştirak etmir.

| Test Sinfi | Əhatə |
|---|---|
| `UserServiceImplTest` | Qeydiyyat (yeni email, dublikat, rol tapılmayanda) |
| `BasketServiceImplTest` | Səbətə əlavə et, artır, sil |
| `OrderServiceImplTest` | Sifariş ver, boş səbət, status yenilə |
| `ProductServiceImplTest` | CRUD — yarat, yenilə, sil, tapılmayan ID |

### İnteqrasiya Testləri (`src/test/.../controllers/`)

`@SpringBootTest` + `MockMvc` + H2 in-memory database ilə bütün stack-i test edir.

| Test Sinfi | Əhatə |
|---|---|
| `AuthControllerIntegrationTest` | Login, qeydiyyat, CSRF qoruması, validation |
| `ProductControllerIntegrationTest` | Admin CRUD, icazə yoxlanması (403/redirect) |

### Testləri icra etmək

```bash
# Bütün testlər
mvnw.cmd test                        # Windows
./mvnw test                          # Linux/macOS

# Yalnız unit testlər
mvnw.cmd test -Dtest="*ServiceImpl*"

# Coverage hesabatı (target/site/jacoco/index.html)
mvnw.cmd verify
```

**Test profili** (`application-test.properties`): H2 in-memory DB, Flyway aktiv, DataSeeder aktiv.

---

## ⚙️ CI/CD

Layihə **GitHub Actions** ilə `.github/workflows/ci.yml` pipeline-ına malikdir:

| Job | Trigger | Açıqlama |
|---|---|---|
| **build-and-test** | Hər push/PR | Maven build + JUnit testlər (PostgreSQL 16 service container ilə). Surefire XML + JaCoCo coverage artifact olaraq yüklənir. |
| **docker-build** | `main` branch-a push | Multi-stage Dockerfile, SHA + `latest` tag ilə image build edilir. Docker Hub push üçün hazır (secrets konfiqurasiyası lazımdır). |
| **security-scan** | `main` branch-a push | OWASP Dependency-Check — CVSS ≥ 9 olan CVE tapıldıqda build uğursuz sayılır. HTML/XML report artifact olaraq saxlanılır. |

**Trigger-lər:** `main` və `develop` branch-larına `push`, `main`-ə `pull_request`.

---

## ✨ Layihənin Xüsusiyyətləri

### ✅ Tamamlanmış

| Xüsusiyyət | Qeyd |
|---|---|
| Məhsul kataloqu | Kateqoriya, rəng, ölçü filtrləri; ColorSize stok cədvəli |
| Çoxlu məhsul şəkli | Photo entity; selected şəkil əsas görünür |
| Səbət sistemi | Əlavə et / artır / sil; istifadəçiyə bağlı |
| Sifariş sistemi | Checkout → PENDING; admin statusu dəyişir |
| İstifadəçi autentifikasiyası | Qeydiyyat, giriş (form login), çıxış |
| Şifrəni unutdum | Forgot password səhifəsi |
| Role-based access | ADMIN / USER — method security + Thymeleaf sec: |
| Admin panel | Dashboard + CRUD (kateqoriya, məhsul, rəng, ölçü, sifariş) |
| Spring Security | BCrypt, CSRF, session fixation mühafizəsi |
| Database migration | Flyway V1-V3 migration skriptləri |
| DataSeeder | İlk açılışda admin istifadəçi + demo data |
| Docker dəstəyi | Multi-stage Dockerfile, non-root user, healthcheck |
| Docker Compose | App + PostgreSQL 16 birlikdə qalxır |
| CI/CD pipeline | GitHub Actions — build, test, Docker, OWASP scan |
| Unit testlər | JUnit 5 + Mockito, 4 service üçün |
| İnteqrasiya testləri | MockMvc + H2, 2 controller üçün |
| Logging | Logback — konsolda rəngli, `logs/` qovluğunda fayl |
| Production konfiqurasiya | Hikari pool, server compression, Actuator `/health` |

### 🚧 Gələcək Planlar

| Xüsusiyyət | Qeyd |
|---|---|
| Ödəniş inteqrasiyası | Stripe / PayPal |
| Email bildirişlər | Sifariş təsdiqi, şifrə sıfırlama (SMTP) |
| REST API | Mobil tətbiq üçün JSON endpoint-lər |
| Elasticsearch | Məhsul tam-mətn axtarışı |
| Redis caching | Kateqoriya / məhsul siyahısı keşləmə |
| Şəkil yükləmə | MultipartFile ilə real fayl yükləmə |

---

## 🗂 Database Schema (Əsas cədvəllər)

```
users ──< user_roles >── roles
products ──< photos
products ──< color_sizes >── colors
              color_sizes >── sizes
users ──< baskets >── products
users ──< orders ──< order_items >── products
categories ──< products
```

**Flyway migration faylları:**

| Fayl | Məzmun |
|---|---|
| `V1__init.sql` | Tam schema — bütün cədvəllər, foreign key-lər, indekslər |
| `V2__fix_order_status_column_type.sql` | `order_status` sütununun tipi düzəldildi |
| `V3__fix_demo_product_photo.sql` | Demo məhsul şəkil URL-i düzəldildi |

---

## 🤝 Contributing

1. Fork the repository
2. Feature branch yaradın: `git checkout -b feature/your-feature-name`
3. Dəyişiklikləri commit edin: `git commit -m "feat: add your feature"`
4. Push edin: `git push origin feature/your-feature-name`
5. Pull Request açın

**Commit mesajı konvensiyası:** [Conventional Commits](https://www.conventionalcommits.org/)
- `feat:` — yeni xüsusiyyət
- `fix:` — xəta düzəltmə
- `refactor:` — yenidən strukturlaşdırma
- `test:` — test əlavəsi / düzəltmə
- `docs:` — sənədləşmə
- `chore:` — alət, konfiqurasiya dəyişikliyi

---

## 📄 Lisenziya

Bu layihə təhsil məqsədlidir. Öyrənmə istinadı olaraq istifadə edə bilərsiniz.

---

*ITBrains Academy — Spring Boot E-Commerce Platform | Java 21 + Spring Boot 3.5*


---

## 📋 Mündəricat

- [Tech Stack](#-tech-stack)
- [Arxitektura](#-arxitektura)
- [Tələblər](#-tələblər)
- [Sürətli Başlama (Docker ilə)](#-sürətli-başlama-docker-ilə)
- [Local Başlama (Docker olmadan)](#-local-başlama-docker-olmadan)
- [Environment Variables](#-environment-variables)
- [Default Giriş Məlumatları](#-default-giriş-məlumatları)
- [CI/CD](#-cicd)
- [Layihənin Xüsusiyyətləri](#-layihənin-xüsusiyyətləri)
- [Routes Overview](#-routes-overview)
- [Contributing](#-contributing)

---

## 🛠 Tech Stack

| Sahə | Texnologiya |
|---|---|
| **Backend** | Java 21, Spring Boot 3.5.7, Spring MVC, Spring Data JPA |
| **Frontend** | Thymeleaf + Layout Dialect, Bootstrap, jQuery |
| **Database** | PostgreSQL 16, Flyway (migration) |
| **Security** | Spring Security, BCrypt, CSRF, Role-based (ADMIN / USER) |
| **Messaging** | Spring AMQP (RabbitMQ) |
| **DevOps** | Docker, Docker Compose, GitHub Actions CI/CD, OWASP Dependency-Check |

---

## 🏗 Arxitektura

Layihə **MVC (Model–View–Controller)** arxitekturasına əsaslanır:

```
Browser  →  Controller  →  Service  →  Repository  →  PostgreSQL
              ↓
          Thymeleaf (HTML)
```

```
src/main/java/az/edu/itbrains/ecommerce/
├── config/           # Konfiqurasiya (ModelMapper, RabbitMQ, Security)
├── controllers/      # HTTP request handler-lər
│   └── admin/        # Admin panel controller-ləri
├── dtos/             # Data Transfer Objects
├── enums/            # Enum-lar (Role)
├── exceptions/       # Custom exception-lar
├── helpers/          # Utility sinifləri
├── models/           # JPA Entity sinifləri
├── repositories/     # Spring Data JPA repository-lər
├── security/         # SecurityConfig, UserDetailsService
└── services/         # Business logic (interface + impls/)
```

---

## 📌 Tələblər

| Alət | Versiya |
|---|---|
| Java (JDK) | 21+ |
| Maven | 3.9+ |
| Docker & Docker Compose | Ən son versiya |
| PostgreSQL | 16 (yalnız local inkişaf üçün — Docker-da avtomatik qaldırılır) |

---

## 🐳 Sürətli Başlama (Docker ilə)

```bash
# 1. Repo-nu klonlayın
git clone https://github.com/AbiyevNurlan/ecommerce-backend.git
cd ecommerce-backend

# 2. Environment faylını yaradın
cp .env.example .env
# .env faylını açın və DB_PASSWORD-u dəyişin

# 3. Docker Compose ilə işə salın
docker-compose up --build -d

# 4. Logları yoxlayın
docker-compose logs -f app
```

Tətbiq **http://localhost:8080** ünvanında qalxacaq.

**Dayandırmaq üçün:**

```bash
docker-compose down          # Konteynerləri dayandır
docker-compose down -v       # + verilənlər bazası data-sını sil
```

---

## 💻 Local Başlama (Docker olmadan)

### 1. PostgreSQL-i qurun və verilənlər bazası yaradın

```sql
CREATE DATABASE ecommerce_db;
```

### 2. Application properties-i konfiqurasiya edin

```bash
cp src/main/resources/application.properties.example src/main/resources/application.properties
```

`application.properties` faylında verilənlər bazası məlumatlarını dəyişin:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/ecommerce_db
spring.datasource.username=postgres
spring.datasource.password=YOUR_PASSWORD
```

> ⚠️ `application.properties` faylı `.gitignore`-dadır — GitHub-a push etməyin!

### 3. Tətbiqi işə salın

```bash
# Linux / macOS
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev

# Windows
mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=dev
```

Tətbiq **http://localhost:8080** ünvanında başlayacaq.

---

## 🔑 Environment Variables

| Dəyişən | Nümunə | Açıqlama |
|---|---|---|
| `DB_NAME` | `ecommerce_db` | PostgreSQL verilənlər bazasının adı |
| `DB_USERNAME` | `postgres` | DB istifadəçi adı |
| `DB_PASSWORD` | `your_password_here` | DB şifrəsi — mütləq dəyişin |
| `DB_HOST` | `db` | DB host (Docker-da `db`, local-da `localhost`) |
| `DB_PORT` | `5432` | PostgreSQL portu |
| `SPRING_PROFILES_ACTIVE` | `prod` | Aktiv profil: `dev`, `prod`, `test` |
| `UPLOAD_DIR` | `/app/uploads` | Yüklənən faylların saxlanılacağı qovluq |

> `.env.example` faylında bütün dəyişənlər Azərbaycanca şərhlərlə izah olunub.

---

## 🔐 Default Giriş Məlumatları

İlk dəfə istifadə üçün verilənlər bazasında istifadəçi yaradın:

| Rol | Qeydiyyat |
|---|---|
| **User** | `/register` səhifəsindən qeydiyyatdan keçin |
| **Admin** | Əvvəlcə qeydiyyatdan keçin, sonra DB-dən `ADMIN` rolunu təyin edin |

Admin rolu təyin etmək üçün:

```sql
-- İstifadəçinin ID-sini tapın
SELECT id FROM users WHERE email = 'your@email.com';

-- Admin rolunu təyin edin
INSERT INTO user_roles (user_id, role_id) VALUES (<user_id>, <admin_role_id>);
```

---

## ⚙️ CI/CD

Layihə **GitHub Actions** ilə avtomatik CI/CD pipeline-a malikdir (`.github/workflows/ci.yml`):

| İş (Job) | Təsvir |
|---|---|
| **Build & Test** | Maven ilə build + testlər (PostgreSQL 16 service ilə). Surefire reports və JaCoCo coverage artifact olaraq yüklənir |
| **Docker Build** | Multi-stage Dockerfile ilə Docker image build edilir. SHA tag + `latest` tag. Docker Hub push üçün hazır (comment-ləri açın) |
| **Security Scan** | OWASP Dependency-Check ilə dependency-lərdə CVE yoxlanılır (CVSS ≥ 9 build-i dayandırır). Report artifact olaraq yüklənir |

**Trigger-lər:** `main` və `develop` branch-larına push, `main`-ə pull request.

---

## ✨ Layihənin Xüsusiyyətləri

### ✅ Tamamlanmış

| Xüsusiyyət | Təsvir |
|---|---|
| Məhsul kataloqu | Kateqoriya, rəng, ölçü, şəkil idarəetməsi |
| Səbət sistemi | Məhsul əlavə et, sil, miqdarı yenilə |
| Sifariş sistemi | Checkout, sifariş tarixi |
| İstifadəçi autentifikasiyası | Qeydiyyat, giriş, çıxış, şifrəni unutdum |
| Admin panel | Dashboard, CRUD əməliyyatları (kateqoriya, məhsul, rəng, ölçü) |
| Spring Security | Form login, BCrypt, CSRF, role-based access |
| Docker dəstəyi | Multi-stage Dockerfile, Docker Compose, non-root user |
| CI/CD | GitHub Actions — build, test, Docker build, security scan |
| Database migration | Flyway ilə versiyalanmış schema migration |
| Unit testlər | JUnit 5 + Mockito ilə service layer testləri |
| İnteqrasiya testləri | Controller layer testləri (MockMvc) |
| Production config | Hikari pool, server compression, Actuator health |

### 🚧 Gələcək planlar

| Xüsusiyyət | Təsvir |
|---|---|
| Ödəniş inteqrasiyası | Stripe / PayPal ilə online ödəniş |
| Email bildirişlər | Sifariş təsdiqi, şifrə sıfırlama |
| REST API | Mobil tətbiq üçün JSON API |
| Axtarış | Elasticsearch ilə məhsul axtarışı |
| Caching | Redis ilə performans optimallaşdırması |

---

## 🗺 Routes Overview

### Storefront

| Method | Route | Təsvir |
|---|---|---|
| GET | `/` | Ana səhifə |
| GET | `/shop` | Bütün məhsullar |
| GET | `/shop/detail/{id}` | Məhsul detalları |
| GET | `/cart` | Səbətə bax |
| POST | `/basket/add` | Səbətə məhsul əlavə et |
| POST | `/basket/remove/{id}` | Səbətdən sil |
| GET | `/cart/checkout` | Checkout səhifəsi |
| POST | `/cart/order` | Sifariş ver |

### Authentication

| Method | Route | Təsvir |
|---|---|---|
| GET/POST | `/login` | Giriş |
| GET/POST | `/register` | Qeydiyyat |
| GET | `/logout` | Çıxış |
| GET | `/forgot-password` | Şifrəni unutdum |

### Admin Panel (`/admin/**` — ADMIN rolu tələb olunur)

| Method | Route | Təsvir |
|---|---|---|
| GET | `/admin` | Dashboard |
| GET/POST | `/admin/categories/**` | Kateqoriya CRUD |
| GET/POST | `/admin/products/**` | Məhsul CRUD |
| GET/POST | `/admin/colors/**` | Rəng CRUD |
| GET/POST | `/admin/sizes/**` | Ölçü CRUD |

---

## 🤝 Contributing

1. Fork the repository
2. Feature branch yaradın: `git checkout -b feature/your-feature-name`
3. Dəyişiklikləri commit edin: `git commit -m "feat: add your feature"`
4. Push edin: `git push origin feature/your-feature-name`
5. Pull Request açın

---

## 📄 Lisenziya

Bu layihə təhsil məqsədlidir. Öyrənmə istinadı olaraq istifadə edə bilərsiniz.

---

*ITBrains Academy — Spring Boot E-Commerce Platform*
