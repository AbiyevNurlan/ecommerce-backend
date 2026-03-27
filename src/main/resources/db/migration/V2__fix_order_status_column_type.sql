-- V2: Convert order_status from SMALLINT (ordinal enum) to VARCHAR (string enum)
-- This fixes the mismatch between @Enumerated(EnumType.STRING) and the existing column type.

-- Step 1: Add temporary VARCHAR column
ALTER TABLE orders ADD COLUMN order_status_new VARCHAR(50);

-- Step 2: Map ordinal values to string values
UPDATE orders SET order_status_new = CASE order_status
    WHEN 0 THEN 'PENDING'
    WHEN 1 THEN 'CONFIRMED'
    WHEN 2 THEN 'PROCESSING'
    WHEN 3 THEN 'SHIPPED'
    WHEN 4 THEN 'IN_TRANSIT'
    WHEN 5 THEN 'OUT_FOR_DELIVERY'
    WHEN 6 THEN 'DELIVERED'
    WHEN 7 THEN 'CANCELLED'
    WHEN 8 THEN 'RETURN_REQUESTED'
    WHEN 9 THEN 'RETURNED'
    WHEN 10 THEN 'FAILED'
    ELSE 'PENDING'
END;

-- Step 3: Drop old column and rename new one
ALTER TABLE orders DROP COLUMN order_status;
ALTER TABLE orders RENAME COLUMN order_status_new TO order_status;

-- Step 4: Set NOT NULL and DEFAULT
ALTER TABLE orders ALTER COLUMN order_status SET NOT NULL;
ALTER TABLE orders ALTER COLUMN order_status SET DEFAULT 'PENDING';
