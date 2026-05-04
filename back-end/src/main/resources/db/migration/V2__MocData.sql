-- 1. Brands
INSERT INTO brands (name) VALUES ('Apple'), ('Samsung'), ('Sony'), ('Asus'), ('Logitech');

-- 2. Users (Admin và Khách hàng)
INSERT INTO users (email, name, password, role) VALUES
('admin@techshop.com', 'Admin TechShop', '$2a$12$b.RkzeAxeIeN7.q0tSJOk.UmozzF7E1jtx.dlIzfBtzYfZDc4mqh6', 'ADMIN'),
('customer@gmail.com', 'Nguyễn Văn Khách', '$2a$12$b.RkzeAxeIeN7.q0tSJOk.UmozzF7E1jtx.dlIzfBtzYfZDc4mqh6', 'CUSTOMER');


-- 3. Categories
INSERT INTO categories (name) VALUES ('Laptop'), ('Điện thoại'), ('Phụ kiện'), ('Âm thanh');

-- 4. Products (Không dùng shop_id nữa)
INSERT INTO products (brand_id, name, quantity, price, status)
SELECT id, 'iPhone 15 Pro Max', 15, 32000000.00, 'ACTIVE' FROM brands WHERE name = 'Apple';

INSERT INTO products (brand_id, name, quantity, price, status)
SELECT id, 'Galaxy S24 Ultra', 10, 29000000.00, 'ACTIVE' FROM brands WHERE name = 'Samsung';

INSERT INTO products (brand_id, name, quantity, price, status)
SELECT id, 'Tai nghe Sony WH-1000XM5', 20, 6500000.00, 'ACTIVE' FROM brands WHERE name = 'Sony';

INSERT INTO products (brand_id, name, quantity, price, status)
SELECT id, 'Chuột không dây MX Master 3S', 50, 2200000.00, 'ACTIVE' FROM brands WHERE name = 'Logitech';

-- 5. Link Categories
INSERT INTO product_categories (product_id, category_id)
SELECT p.id, c.id FROM products p, categories c 
WHERE p.name = 'iPhone 15 Pro Max' AND c.name = 'Điện thoại';

INSERT INTO product_categories (product_id, category_id)
SELECT p.id, c.id FROM products p, categories c 
WHERE p.name = 'Tai nghe Sony WH-1000XM5' AND c.name = 'Âm thanh';

-- 6. Giỏ hàng mẫu cho Khách
INSERT INTO carts (customer_id, total_product, total_price)
SELECT id, 0, 0 FROM users WHERE email = 'customer@gmail.com';