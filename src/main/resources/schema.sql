-- Drop tables if they exist to ensure a clean slate on each run
DROP TABLE IF EXISTS shop_front_social_links;
DROP TABLE IF EXISTS product_images;
DROP TABLE IF EXISTS order_items;
DROP TABLE IF EXISTS reviews;
DROP TABLE IF EXISTS carts;
DROP TABLE IF EXISTS cart_items;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS reports;
DROP TABLE IF EXISTS shop_fronts;
DROP TABLE IF EXISTS delivery_partners;
DROP TABLE IF EXISTS delivery_vehicles;
DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS users;

-- Create users table
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    apartment_number VARCHAR(255),
    email VARCHAR(255) UNIQUE,
    phone VARCHAR(255) UNIQUE,
    user_type VARCHAR(50),
    shop_name VARCHAR(255),
    is_verified BOOLEAN,
    is_blocked BOOLEAN,
    upi_id VARCHAR(255),
    payment_qr_url VARCHAR(255)
);

-- Create products table
CREATE TABLE products (
    id VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(10, 2) NOT NULL,
    category VARCHAR(255),
    seller_id BIGINT,
    stock INT,
    status VARCHAR(255),
    posted_date TIMESTAMP WITH TIME ZONE
);

-- Create product_images table
CREATE TABLE product_images (
    product_id VARCHAR(255) NOT NULL,
    image_url VARCHAR(255),
    FOREIGN KEY (product_id) REFERENCES products(id)
);

-- Create delivery_vehicles table
CREATE TABLE delivery_vehicles (
    id VARCHAR(255) PRIMARY KEY,
    vehicle_number VARCHAR(255) NOT NULL UNIQUE,
    vehicle_type VARCHAR(50) NOT NULL
);

-- Create delivery_partners table
CREATE TABLE delivery_partners (
    id VARCHAR(255) PRIMARY KEY,
    user_id BIGINT,
    name VARCHAR(255),
    phone VARCHAR(255),
    is_available BOOLEAN,
    active_deliveries INT,
    lat DOUBLE,
    lng DOUBLE,
    vehicle_id VARCHAR(255),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (vehicle_id) REFERENCES delivery_vehicles(id)
);

-- Create orders table
CREATE TABLE orders (
    id VARCHAR(255) PRIMARY KEY,
    buyer_id BIGINT,
    total_amount DECIMAL(10, 2),
    order_date TIMESTAMP WITH TIME ZONE,
    status VARCHAR(50),
    fulfillment_method VARCHAR(50),
    payment_method VARCHAR(50),
    delivery_partner_id VARCHAR(255),
    FOREIGN KEY (buyer_id) REFERENCES users(id),
    FOREIGN KEY (delivery_partner_id) REFERENCES delivery_partners(id)
);

-- Add other table creation scripts here (carts, reviews, etc.) as needed.
-- This is a minimal schema to get the application running.