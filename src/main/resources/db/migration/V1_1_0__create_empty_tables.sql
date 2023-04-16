START TRANSACTION;

USE gs;

CREATE TABLE tag (
                             id INT AUTO_INCREMENT PRIMARY KEY,
                             name VARCHAR(20) UNIQUE NOT NULL                            
) ENGINE=INNODB;

CREATE TABLE gift_certificate (
                              id INT AUTO_INCREMENT PRIMARY KEY,
                              name VARCHAR(256) UNIQUE NOT NULL,
                              description TEXT,
                              price INT NOT NULL,
                              duration INT NOT NULL,
                              create_date DATE NOT NULL,
                              last_update_date DATE NOT NULL
) ENGINE=INNODB;

CREATE TABLE tag_js_junction (
                             id INT AUTO_INCREMENT PRIMARY KEY,
                             tag_id INT NOT NULL,
                             gs_id INT NOT NULL, 
                             FOREIGN KEY (tag_id) REFERENCES tag(id) ON UPDATE RESTRICT ON DELETE RESTRICT,
                             FOREIGN KEY (gs_id) REFERENCES gift_certificate(id) ON UPDATE RESTRICT ON DELETE RESTRICT
) ENGINE=INNODB;

COMMIT;