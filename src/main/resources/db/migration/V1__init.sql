-- ============================================================
-- V1__init.sql  —  Initial schema for ecommerce application
-- Spring Boot 3.5.7 / PostgreSQL
-- ============================================================

-- ── roles ─────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS role (
    id   BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

-- ── users ─────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS users (
    id                      BIGSERIAL    PRIMARY KEY,
    name                    VARCHAR(255),
    surname                 VARCHAR(255),
    email                   VARCHAR(255) NOT NULL UNIQUE,
    password                VARCHAR(255) NOT NULL,
    enabled                 BOOLEAN      NOT NULL DEFAULT TRUE,
    account_non_expired     BOOLEAN      NOT NULL DEFAULT TRUE,
    account_non_locked      BOOLEAN      NOT NULL DEFAULT TRUE,
    credentials_non_expired BOOLEAN      NOT NULL DEFAULT TRUE
);

-- ── user_roles  (join table) ────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS user_roles (
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    role_id BIGINT NOT NULL REFERENCES role(id)  ON DELETE CASCADE,
    PRIMARY KEY (user_id, role_id)
);

-- ── category ──────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS category (
    id       BIGSERIAL    PRIMARY KEY,
    name     VARCHAR(255),
    seo_url  VARCHAR(255),
    featured BOOLEAN      NOT NULL DEFAULT FALSE
);

-- ── color ─────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS color (
    id   BIGSERIAL    PRIMARY KEY,
    name VARCHAR(255)
);

-- ── size ──────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS size (
    id   BIGSERIAL    PRIMARY KEY,
    size VARCHAR(255)
);

-- ── product ───────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS product (
    id                BIGSERIAL     PRIMARY KEY,
    name              VARCHAR(255),
    description       VARCHAR(1000),
    short_description VARCHAR(255),
    specification     VARCHAR(255),
    price             FLOAT8        NOT NULL DEFAULT 0,
    discount          FLOAT8        NOT NULL DEFAULT 0,
    barcode           VARCHAR(255),
    featured          BOOLEAN       NOT NULL DEFAULT FALSE,
    hot_trending      BOOLEAN       NOT NULL DEFAULT FALSE,
    category_id       BIGINT        REFERENCES category(id) ON DELETE SET NULL
);

-- ── color_size  (product variants) ────────────────────────────────────────
CREATE TABLE IF NOT EXISTS color_size (
    id         BIGSERIAL PRIMARY KEY,
    quantity   INT       NOT NULL DEFAULT 0,
    color_id   BIGINT    REFERENCES color(id)   ON DELETE SET NULL,
    size_id    BIGINT    REFERENCES size(id)     ON DELETE SET NULL,
    product_id BIGINT    REFERENCES product(id)  ON DELETE CASCADE
);

-- ── photo ─────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS photo (
    id         BIGSERIAL PRIMARY KEY,
    url        VARCHAR(500),
    selected   BOOLEAN   NOT NULL DEFAULT FALSE,
    product_id BIGINT    REFERENCES product(id) ON DELETE CASCADE
);

-- ── basket ────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS basket (
    id         BIGSERIAL PRIMARY KEY,
    quantity   INT       NOT NULL DEFAULT 1,
    user_id    BIGINT    REFERENCES users(id)   ON DELETE CASCADE,
    product_id BIGINT    REFERENCES product(id) ON DELETE CASCADE
);

-- ── orders ────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS orders (
    id           BIGSERIAL    PRIMARY KEY,
    order_status VARCHAR(50)  NOT NULL DEFAULT 'PENDING',
    address      VARCHAR(500),
    user_id      BIGINT       REFERENCES users(id) ON DELETE SET NULL
);

-- ── order_item ────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS order_item (
    id         BIGSERIAL PRIMARY KEY,
    price      FLOAT8,
    quantity   INT       NOT NULL DEFAULT 1,
    product_id BIGINT    REFERENCES product(id) ON DELETE SET NULL,
    order_id   BIGINT    REFERENCES orders(id)  ON DELETE CASCADE
);
