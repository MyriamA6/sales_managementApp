-- Suppression des tables si elles existent déjà
DROP TABLE IF EXISTS Content;
DROP TABLE IF EXISTS Pants;
DROP TABLE IF EXISTS Top;
DROP TABLE IF EXISTS Product;
DROP TABLE IF EXISTS Invoice;
DROP TABLE IF EXISTS Order_record;
DROP TABLE IF EXISTS Customer;

-- Création de la table Customer
CREATE TABLE Customer (
                          customer_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          first_name VARCHAR(50) NOT NULL,
                          last_name VARCHAR(50) NOT NULL,
                          email VARCHAR(100) UNIQUE,
                          address VARCHAR(100) NOT NULL,
                          phone_number VARCHAR(20),
                          login_name VARCHAR(30) NOT NULL UNIQUE,
                          user_password VARCHAR(256) NOT NULL
);

-- Création de la table Order_record
CREATE TABLE Order_record (
                              order_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              customer_id BIGINT NOT NULL,
                              total_price INT NOT NULL,
                              order_date DATE,
                              order_state VARCHAR(50) NOT NULL,
                              CONSTRAINT FK_Customer_Order_record
                                  FOREIGN KEY (customer_id) REFERENCES Customer(customer_id),
                              CONSTRAINT CK_order_state
                                  CHECK (order_state IN ('in progress', 'payed', 'delivered'))
);

-- Création de la table Invoice
CREATE TABLE Invoice (
                         invoice_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         order_id BIGINT UNIQUE NOT NULL,
                         invoice_date DATE,
                         CONSTRAINT FK_order_id
                             FOREIGN KEY (order_id) REFERENCES Order_record(order_id)
);

-- Création de la table Product
CREATE TABLE Product (
                         product_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         name VARCHAR(50) NOT NULL,
                         price INT NOT NULL,
                         stock INT NOT NULL,
                         gender VARCHAR(30) NOT NULL,
                         color VARCHAR(30) NOT NULL,
                         size INT NOT NULL,
                         description VARCHAR(300),
                         CONSTRAINT CK_size
                             CHECK (size IN (32, 34, 36, 38, 40, 42, 44, 46)),
                         CONSTRAINT CK_gender
                             CHECK (gender IN ('Male', 'Female', 'Unisex')),
                         CONSTRAINT CK_Unique UNIQUE (name, size, color, gender)
);

-- Création de la table Pants
CREATE TABLE Pants (
                       product_id BIGINT UNIQUE PRIMARY KEY,
                       length VARCHAR(30) NOT NULL,
                       CONSTRAINT CK_length CHECK (length IN ('Shorts', 'Regular')),
                       CONSTRAINT FK_Pants_Product FOREIGN KEY (product_id) REFERENCES Product(product_id)
);

-- Création de la table Top
CREATE TABLE Top (
                     product_id BIGINT UNIQUE PRIMARY KEY,
                     sleevesType VARCHAR(30) NOT NULL,
                     CONSTRAINT CK_sleevesType CHECK (sleevesType IN ('T-shirt', 'Sweater')),
                     CONSTRAINT FK_Top_Product FOREIGN KEY (product_id) REFERENCES Product(product_id)
);

-- Création de la table Content
CREATE TABLE Content (
                         product_id BIGINT NOT NULL,
                         order_id BIGINT NOT NULL,
                         quantity_ordered INT NOT NULL,
                         PRIMARY KEY (product_id, order_id),
                         CONSTRAINT FK_Content_Product FOREIGN KEY (product_id) REFERENCES Product(product_id),
                         CONSTRAINT FK_Content_Order FOREIGN KEY (order_id) REFERENCES Order_record(order_id)
);

-- Insertion des clients
-- Julien mdp : 123456
-- Louis mdp : louis
-- Marie mdp : 0000
-- Pierre mdp : java

INSERT INTO Customer (first_name, last_name, email, address, phone_number, login_name, user_password)
VALUES
    ('Julien', 'Leclerc', 'julien.leclerc@gmail.com', '30 Avenue de Paris, Paris', '0637483990', 'Julien10', 'e10adc3949ba59abbe56e057f20f883e'),
    ('Louis', 'Jean', 'louis.jean@gmail.com', '30 Avenue du Marais, Paris', '0974663790', 'Louis', '777cadc280bb23ebea268ded98338c39'),
    ('Marie', 'Dupont', 'marie.dupont@gmail.com', '15 Rue de Lyon, Lyon', '0647896541', 'Marie', '4a7d1ed414474e4033ac29ccb8653d9b'),
    ('Pierre', 'Martin', 'pierre.martin@gmail.com', '10 Rue de Lille, Lille', '0623987654', 'Pm', '93f725a07423fe1c889f448b33d21f46');

-- Insertion des produits
INSERT INTO Product (name, price, stock, gender, color, size, description)
VALUES
    ('Sweatshirt', 40, 2, 'Unisex', 'Grey', 38, 'A comfortable sweatshirt'),
    ('Chino Pants', 60, 15, 'Male', 'Beige', 34, 'Elegant chino pants'),
    ('Blouse', 35, 25, 'Female', 'White', 36, 'Light cotton blouse'),
    ('Cargo Pants', 55, 10, 'Unisex', 'Black', 38, 'Practical cargo pants'),
    ('Hoodie', 45, 30, 'Unisex', 'Navy Blue', 40, 'Warm and cozy hoodie'),
    ('Formal Shirt', 50, 20, 'Male', 'Blue', 42, 'Elegant formal shirt');

-- Insertion des pantalons
INSERT INTO Pants (product_id, length)
VALUES
    (2, 'Regular'),  -- Chino Pants
    (4, 'Shorts');  -- Cargo Pants

-- Insertion des hauts
INSERT INTO Top (product_id, sleevesType)
VALUES
    (1, 'Sweater'),  -- Sweatshirt
    (3, 'T-shirt'),  -- Blouse
    (5, 'Sweater'),  -- Hoodie
    (6, 'T-shirt');   -- Formal Shirt

-- Insertion des commandes
INSERT INTO Order_record (customer_id, total_price, order_date, order_state)
VALUES
    (1, 1350, DATE '2024-10-15', 'payed'),
    (2, 1000, DATE '2024-10-16', 'in progress'),
    (3, 800, DATE '2024-10-14', 'delivered'),
    (4, 1500, DATE '2024-10-12', 'delivered');

-- Insertion des factures
INSERT INTO Invoice (order_id, invoice_date)
VALUES
    (1, DATE '2024-10-16'),
    (3, DATE '2024-10-15'),
    (4, DATE '2024-10-13');

-- Insertion des contenus des commandes
INSERT INTO Content (product_id, order_id, quantity_ordered)
VALUES
    (1, 1, 1),
    (4, 1, 1),
    (2, 2, 1),
    (3, 3, 1);
