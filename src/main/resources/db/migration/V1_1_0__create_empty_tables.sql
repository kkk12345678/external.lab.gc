CREATE TABLE tags (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(256) UNIQUE NOT NULL
);

CREATE TABLE gift_certificates (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(256) UNIQUE NOT NULL,
    description TEXT,
    price DECIMAL(10, 2) NOT NULL,
    duration INT NOT NULL,
    create_date VARCHAR(20) NOT NULL,
    last_update_date VARCHAR(20) NOT NULL
);

CREATE TABLE gift_certificate_tags (
    gift_certificate_id INT NOT NULL,
    tag_id INT NOT NULL,
    PRIMARY KEY (gift_certificate_id, tag_id),
    FOREIGN KEY (tag_id) REFERENCES tags(id) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (gift_certificate_id) REFERENCES gift_certificates(id) ON UPDATE CASCADE ON DELETE CASCADE
);