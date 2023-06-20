DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS roles;

CREATE TABLE roles (
	role_id INT AUTO_INCREMENT PRIMARY KEY,
    role_name VARCHAR(20) UNIQUE NOT NULL
);

CREATE TABLE users (
	user_id INT AUTO_INCREMENT PRIMARY KEY,
    user_name VARCHAR(255) UNIQUE NOT NULL,
    role_id INT NOT NULL,
    user_password VARCHAR(16),
    FOREIGN KEY (role_id) REFERENCES roles(role_id) ON UPDATE RESTRICT ON DELETE RESTRICT
);

DROP TABLE IF EXISTS orders;

CREATE TABLE orders (
    order_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    gift_certificate_id INT NOT NULL,
    sum DECIMAL(10, 2) NOT NULL,
    created_date DATETIME NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON UPDATE RESTRICT ON DELETE RESTRICT,
    FOREIGN KEY (gift_certificate_id) REFERENCES gift_certificates(gift_certificate_id) ON UPDATE RESTRICT ON DELETE RESTRICT
);

INSERT INTO roles(role_id, role_name) VALUES(default, 'admin');
INSERT INTO roles(role_id, role_name) VALUES(default, 'user');