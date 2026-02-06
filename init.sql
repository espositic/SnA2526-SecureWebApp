CREATE DATABASE IF NOT EXISTS secure_app_db;
USE secure_app_db;

CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT IGNORE INTO users (email, password_hash) VALUES 
('test@test.com', '$2a$12$FIMbGsuF8fYY73iaXLTeTuITqdZAXKtJ4yFcE6/gWKvtedzVn5d9q');