-- ============================================================
-- V5__add_demo_products.sql
-- Add demo products for Women's and Men's categories
-- so the shop looks full when browsing by category
-- ============================================================

-- Ensure categories exist (idempotent via seo_url)
INSERT INTO category (name, seo_url, featured)
SELECT 'Women', 'women', true
WHERE NOT EXISTS (SELECT 1 FROM category WHERE seo_url = 'women');

INSERT INTO category (name, seo_url, featured)
SELECT 'Men', 'men', true
WHERE NOT EXISTS (SELECT 1 FROM category WHERE seo_url = 'men');

INSERT INTO category (name, seo_url, featured)
SELECT 'Kids', 'kids', false
WHERE NOT EXISTS (SELECT 1 FROM category WHERE seo_url = 'kids');

INSERT INTO category (name, seo_url, featured)
SELECT 'Accessories', 'accessories', false
WHERE NOT EXISTS (SELECT 1 FROM category WHERE seo_url = 'accessories');

INSERT INTO category (name, seo_url, featured)
SELECT 'Cosmetic', 'cosmetic', false
WHERE NOT EXISTS (SELECT 1 FROM category WHERE seo_url = 'cosmetic');

-- ── Women's products ──────────────────────────────────────────────────────
INSERT INTO product (name, description, short_description, specification, price, discount, barcode, featured, hot_trending, category_id, product_status)
VALUES
('Elegant Summer Dress',     'A beautiful floral summer dress perfect for warm days. Made with breathable cotton fabric.', 'Floral summer dress',     '', 79.99, 10, 'W-DRESS-001',  true,  false, (SELECT id FROM category WHERE seo_url='women'), 'ACTIVE'),
('Classic White Blouse',     'Timeless white blouse with delicate lace details. Ideal for both office and casual wear.', 'White lace blouse',       '', 49.99, 0,  'W-BLOUSE-001', false, false, (SELECT id FROM category WHERE seo_url='women'), 'ACTIVE'),
('High-Waist Skinny Jeans',  'Stretch denim skinny jeans with a flattering high waist. Available in dark wash.', 'Dark wash skinny jeans',  '', 59.99, 5,  'W-JEANS-001',  false, true,  (SELECT id FROM category WHERE seo_url='women'), 'ACTIVE'),
('Wool Blend Winter Coat',   'Premium wool blend coat with double-breasted buttons. Perfect for the cold season.', 'Double-breasted wool coat','', 149.99,20, 'W-COAT-001',   true,  false, (SELECT id FROM category WHERE seo_url='women'), 'ACTIVE'),
('Silk Evening Gown',        'Luxurious silk evening gown with a flowing silhouette. Ideal for formal occasions.', 'Silk formal gown',        '', 199.99, 0, 'W-GOWN-001',   false, true,  (SELECT id FROM category WHERE seo_url='women'), 'ACTIVE'),
('Casual Knit Sweater',      'Soft cashmere-blend knit sweater. Cozy and stylish for everyday wear.', 'Cashmere knit sweater',   '', 69.99, 8,  'W-SWEAT-001',  false, false, (SELECT id FROM category WHERE seo_url='women'), 'ACTIVE'),
('Pleated Midi Skirt',       'Elegant pleated midi skirt in satin finish. Pairs perfectly with any blouse.', 'Satin pleated skirt',     '', 44.99, 0,  'W-SKIRT-001',  false, false, (SELECT id FROM category WHERE seo_url='women'), 'ACTIVE'),
('Leather Crossbody Bag',    'Genuine leather crossbody bag with adjustable strap. Compact yet spacious.', 'Leather crossbody bag',   '', 89.99,15,  'W-BAG-001',    true,  false, (SELECT id FROM category WHERE seo_url='women'), 'ACTIVE');

-- ── Men's products ────────────────────────────────────────────────────────
INSERT INTO product (name, description, short_description, specification, price, discount, barcode, featured, hot_trending, category_id, product_status)
VALUES
('Slim Fit Navy Suit',       'Classic slim fit navy suit crafted from Italian wool. Two-button closure with notch lapel.', 'Italian wool navy suit',  '', 299.99,30, 'M-SUIT-001',   true,  false, (SELECT id FROM category WHERE seo_url='men'), 'ACTIVE'),
('Oxford Button-Down Shirt', 'Premium cotton Oxford shirt with button-down collar. A wardrobe essential.', 'Cotton Oxford shirt',     '', 54.99, 0,  'M-SHIRT-001',  false, false, (SELECT id FROM category WHERE seo_url='men'), 'ACTIVE'),
('Straight Fit Chinos',      'Comfortable stretch chinos in khaki. Perfect for smart-casual occasions.', 'Khaki stretch chinos',    '', 44.99, 5,  'M-CHINO-001',  false, true,  (SELECT id FROM category WHERE seo_url='men'), 'ACTIVE'),
('Leather Bomber Jacket',    'Genuine leather bomber jacket with quilted lining. Rugged yet refined.', 'Leather bomber jacket',   '', 179.99,25, 'M-JACKET-001', true,  false, (SELECT id FROM category WHERE seo_url='men'), 'ACTIVE'),
('Merino Wool V-Neck',       'Fine merino wool V-neck sweater. Lightweight and perfect for layering.', 'Merino V-neck sweater',   '', 64.99, 0,  'M-VNECK-001',  false, false, (SELECT id FROM category WHERE seo_url='men'), 'ACTIVE'),
('Denim Trucker Jacket',     'Classic denim trucker jacket in medium wash. Iconic style for any season.', 'Denim trucker jacket',    '', 74.99,10,  'M-DENIM-001',  false, true,  (SELECT id FROM category WHERE seo_url='men'), 'ACTIVE'),
('Casual Polo Shirt',        'Breathable piqué cotton polo shirt. Available in multiple colors.', 'Piqué cotton polo',       '', 39.99, 0,  'M-POLO-001',   false, false, (SELECT id FROM category WHERE seo_url='men'), 'ACTIVE'),
('Performance Sneakers',     'Lightweight performance sneakers with cushioned sole. Great for daily wear.', 'Cushioned sneakers',      '', 99.99,12,  'M-SHOE-001',   true,  false, (SELECT id FROM category WHERE seo_url='men'), 'ACTIVE');

-- ── Kids products ─────────────────────────────────────────────────────────
INSERT INTO product (name, description, short_description, specification, price, discount, barcode, featured, hot_trending, category_id, product_status)
VALUES
('Kids Rainbow T-Shirt',     'Fun rainbow printed t-shirt for kids. 100% organic cotton.', 'Rainbow print tee',       '', 19.99, 0,  'K-TEE-001',    false, false, (SELECT id FROM category WHERE seo_url='kids'), 'ACTIVE'),
('Kids Denim Overalls',      'Cute denim overalls with adjustable straps. Durable and machine washable.', 'Denim overalls',          '', 34.99, 5,  'K-OVER-001',   false, true,  (SELECT id FROM category WHERE seo_url='kids'), 'ACTIVE'),
('Kids Winter Puffer Jacket','Warm puffer jacket with hood. Water-resistant shell keeps kids dry.', 'Hooded puffer jacket',    '', 49.99, 8,  'K-PUFF-001',   true,  false, (SELECT id FROM category WHERE seo_url='kids'), 'ACTIVE');

-- ── Accessories products ──────────────────────────────────────────────────
INSERT INTO product (name, description, short_description, specification, price, discount, barcode, featured, hot_trending, category_id, product_status)
VALUES
('Classic Aviator Sunglasses','Polarized aviator sunglasses with UV400 protection. Gold metal frame.', 'Gold aviator sunglasses', '', 29.99, 0,  'A-SUN-001',    false, false, (SELECT id FROM category WHERE seo_url='accessories'), 'ACTIVE'),
('Leather Belt',             'Genuine leather belt with brushed silver buckle. 35mm width.', 'Silver buckle belt',      '', 24.99, 0,  'A-BELT-001',   false, false, (SELECT id FROM category WHERE seo_url='accessories'), 'ACTIVE'),
('Silk Scarf',               'Hand-printed silk scarf in vibrant geometric pattern. 90x90cm.', 'Geometric silk scarf',    '', 39.99, 5,  'A-SCRF-001',   false, true,  (SELECT id FROM category WHERE seo_url='accessories'), 'ACTIVE');

-- ── Cosmetic products ─────────────────────────────────────────────────────
INSERT INTO product (name, description, short_description, specification, price, discount, barcode, featured, hot_trending, category_id, product_status)
VALUES
('Matte Lipstick Set',       'Set of 6 matte liquid lipsticks in trending shades. Long-lasting formula.', 'Matte lipstick set',      '', 34.99, 0,  'C-LIP-001',    false, false, (SELECT id FROM category WHERE seo_url='cosmetic'), 'ACTIVE'),
('Vitamin C Serum',          'Brightening vitamin C serum with hyaluronic acid. 30ml dropper bottle.', 'Vitamin C face serum',    '', 24.99, 3,  'C-SER-001',    false, true,  (SELECT id FROM category WHERE seo_url='cosmetic'), 'ACTIVE'),
('Eyeshadow Palette',        'Professional 12-shade eyeshadow palette. Mix of matte and shimmer finishes.', 'Pro eyeshadow palette',   '', 44.99, 7,  'C-EYE-001',    true,  false, (SELECT id FROM category WHERE seo_url='cosmetic'), 'ACTIVE');

-- ── Photos for new products ───────────────────────────────────────────────
-- Assign product images from existing static assets (cycling through 8 available images).
-- Women's products (8 products)
INSERT INTO photo (url, selected, product_id) SELECT '/front/img/product/product-2.jpg', true, id FROM product WHERE barcode='W-DRESS-001';
INSERT INTO photo (url, selected, product_id) SELECT '/front/img/product/product-3.jpg', true, id FROM product WHERE barcode='W-BLOUSE-001';
INSERT INTO photo (url, selected, product_id) SELECT '/front/img/product/product-4.jpg', true, id FROM product WHERE barcode='W-JEANS-001';
INSERT INTO photo (url, selected, product_id) SELECT '/front/img/product/product-5.jpg', true, id FROM product WHERE barcode='W-COAT-001';
INSERT INTO photo (url, selected, product_id) SELECT '/front/img/product/product-6.jpg', true, id FROM product WHERE barcode='W-GOWN-001';
INSERT INTO photo (url, selected, product_id) SELECT '/front/img/product/product-7.jpg', true, id FROM product WHERE barcode='W-SWEAT-001';
INSERT INTO photo (url, selected, product_id) SELECT '/front/img/product/product-8.jpg', true, id FROM product WHERE barcode='W-SKIRT-001';
INSERT INTO photo (url, selected, product_id) SELECT '/front/img/product/product-2.jpg', true, id FROM product WHERE barcode='W-BAG-001';

-- Men's products (8 products)
INSERT INTO photo (url, selected, product_id) SELECT '/front/img/product/product-3.jpg', true, id FROM product WHERE barcode='M-SUIT-001';
INSERT INTO photo (url, selected, product_id) SELECT '/front/img/product/product-4.jpg', true, id FROM product WHERE barcode='M-SHIRT-001';
INSERT INTO photo (url, selected, product_id) SELECT '/front/img/product/product-5.jpg', true, id FROM product WHERE barcode='M-CHINO-001';
INSERT INTO photo (url, selected, product_id) SELECT '/front/img/product/product-6.jpg', true, id FROM product WHERE barcode='M-JACKET-001';
INSERT INTO photo (url, selected, product_id) SELECT '/front/img/product/product-7.jpg', true, id FROM product WHERE barcode='M-VNECK-001';
INSERT INTO photo (url, selected, product_id) SELECT '/front/img/product/product-8.jpg', true, id FROM product WHERE barcode='M-DENIM-001';
INSERT INTO photo (url, selected, product_id) SELECT '/front/img/product/product-1.jpg', true, id FROM product WHERE barcode='M-POLO-001';
INSERT INTO photo (url, selected, product_id) SELECT '/front/img/product/product-2.jpg', true, id FROM product WHERE barcode='M-SHOE-001';

-- Kids products (3 products)
INSERT INTO photo (url, selected, product_id) SELECT '/front/img/product/product-3.jpg', true, id FROM product WHERE barcode='K-TEE-001';
INSERT INTO photo (url, selected, product_id) SELECT '/front/img/product/product-4.jpg', true, id FROM product WHERE barcode='K-OVER-001';
INSERT INTO photo (url, selected, product_id) SELECT '/front/img/product/product-5.jpg', true, id FROM product WHERE barcode='K-PUFF-001';

-- Accessories products (3 products)
INSERT INTO photo (url, selected, product_id) SELECT '/front/img/product/product-6.jpg', true, id FROM product WHERE barcode='A-SUN-001';
INSERT INTO photo (url, selected, product_id) SELECT '/front/img/product/product-7.jpg', true, id FROM product WHERE barcode='A-BELT-001';
INSERT INTO photo (url, selected, product_id) SELECT '/front/img/product/product-8.jpg', true, id FROM product WHERE barcode='A-SCRF-001';

-- Cosmetic products (3 products)
INSERT INTO photo (url, selected, product_id) SELECT '/front/img/product/product-1.jpg', true, id FROM product WHERE barcode='C-LIP-001';
INSERT INTO photo (url, selected, product_id) SELECT '/front/img/product/product-2.jpg', true, id FROM product WHERE barcode='C-SER-001';
INSERT INTO photo (url, selected, product_id) SELECT '/front/img/product/product-3.jpg', true, id FROM product WHERE barcode='C-EYE-001';
