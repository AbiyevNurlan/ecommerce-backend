-- ============================================================
-- V4__add_seller_promotion.sql
-- Multi-vendor (Seller) and Promotion system
-- ============================================================

-- ── ROLE_SELLER ───────────────────────────────────────────────────────────
INSERT INTO role (name) VALUES ('ROLE_SELLER') ON CONFLICT (name) DO NOTHING;

-- ── sellers ───────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS sellers (
    id               BIGSERIAL      PRIMARY KEY,
    user_id          BIGINT         NOT NULL UNIQUE REFERENCES users(id) ON DELETE CASCADE,
    shop_name        VARCHAR(100)   NOT NULL UNIQUE,
    shop_description TEXT,
    balance          DECIMAL(10,2)  NOT NULL DEFAULT 0.00,
    commission_rate  DECIMAL(5,2)   NOT NULL DEFAULT 10.00,
    is_approved      BOOLEAN        NOT NULL DEFAULT FALSE,
    created_at       TIMESTAMP      NOT NULL DEFAULT NOW()
);

-- ── product: add seller_id and product_status ─────────────────────────────
ALTER TABLE product
    ADD COLUMN IF NOT EXISTS seller_id     BIGINT      REFERENCES sellers(id) ON DELETE SET NULL,
    ADD COLUMN IF NOT EXISTS product_status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE';

-- ── promotions ────────────────────────────────────────────────────────────
-- promotion_type : FEATURED | SPONSORED | HOT_TRENDING
-- status         : PENDING  | ACTIVE    | EXPIRED | CANCELLED
CREATE TABLE IF NOT EXISTS promotions (
    id             BIGSERIAL      PRIMARY KEY,
    product_id     BIGINT         NOT NULL REFERENCES product(id)  ON DELETE CASCADE,
    seller_id      BIGINT         NOT NULL REFERENCES sellers(id)  ON DELETE CASCADE,
    promotion_type VARCHAR(30)    NOT NULL,
    start_date     TIMESTAMP      NOT NULL,
    end_date       TIMESTAMP      NOT NULL,
    amount_paid    DECIMAL(10,2)  NOT NULL,
    status         VARCHAR(20)    NOT NULL DEFAULT 'PENDING',
    created_at     TIMESTAMP      NOT NULL DEFAULT NOW()
);

-- ── seller_transactions ───────────────────────────────────────────────────
-- transaction_type : CREDIT | PROMO_DEBIT | COMMISSION_DEBIT
CREATE TABLE IF NOT EXISTS seller_transactions (
    id               BIGSERIAL      PRIMARY KEY,
    seller_id        BIGINT         NOT NULL REFERENCES sellers(id) ON DELETE CASCADE,
    amount           DECIMAL(10,2)  NOT NULL,
    transaction_type VARCHAR(30)    NOT NULL,
    description      VARCHAR(255),
    created_at       TIMESTAMP      NOT NULL DEFAULT NOW()
);

-- ── indexes ───────────────────────────────────────────────────────────────
CREATE INDEX IF NOT EXISTS idx_promotions_product    ON promotions(product_id);
CREATE INDEX IF NOT EXISTS idx_promotions_seller     ON promotions(seller_id);
CREATE INDEX IF NOT EXISTS idx_promotions_status     ON promotions(status);
CREATE INDEX IF NOT EXISTS idx_promotions_end_date   ON promotions(end_date);
CREATE INDEX IF NOT EXISTS idx_seller_tx_seller      ON seller_transactions(seller_id);
CREATE INDEX IF NOT EXISTS idx_product_seller        ON product(seller_id);
CREATE INDEX IF NOT EXISTS idx_product_status        ON product(product_status);
