-- Suppress tables if they already exist
DROP TABLE IF EXISTS Content;
DROP TABLE IF EXISTS Pants;
DROP TABLE IF EXISTS Top;
DROP TABLE IF EXISTS Product;
DROP TABLE IF EXISTS Invoice;
DROP TABLE IF EXISTS Order_record;
DROP TABLE IF EXISTS Customer;

-- Create the Customer table
CREATE TABLE Customer (
                          customer_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          first_name VARCHAR(50) NOT NULL,
                          last_name VARCHAR(50) NOT NULL,
                          email VARCHAR(100),
                          address VARCHAR(100) NOT NULL,
                          phone_number VARCHAR(20),
                          login_name VARCHAR(30) NOT NULL,
                          user_password VARCHAR(30) NOT NULL
);

-- Create the Order_record table
CREATE TABLE Order_record (
                              order_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              customer_id BIGINT NOT NULL,
                              total_price INTEGER NOT NULL,
                              order_date DATE, -- DEFAULT CURRENT_DATE,
                              order_state VARCHAR(50) NOT NULL,
                              CONSTRAINT FK_Customer_Order_record
                                  FOREIGN KEY (customer_id) REFERENCES Customer(customer_id),
                              CONSTRAINT CK_order_state
                                  CHECK (order_state IN ('in progress', 'confirmed', 'delivered'))
);

-- Create the Invoice table
CREATE TABLE Invoice (
                         invoice_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         order_id BIGINT UNIQUE NOT NULL,
                         invoice_date DATE,
                         CONSTRAINT FK_order_id
                             FOREIGN KEY (order_id) REFERENCES Order_record(order_id)
);

-- Create the Product table
CREATE TABLE Product (
                         product_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         name VARCHAR(50) UNIQUE NOT NULL,
                         price INTEGER NOT NULL,
                         stock INTEGER NOT NULL,
                         gender VARCHAR(30) NOT NULL,
                         color VARCHAR(30) NOT NULL,
                         size INT NOT NULL,
                         description VARCHAR(300),
                         CONSTRAINT CK_size
                             CHECK (size IN (32, 34, 36, 38, 40, 42, 44, 46)),
                         CONSTRAINT CK_gender
                             CHECK (gender IN ('Male', 'Female', 'Unisex'))
);

-- Create the Pants table
CREATE TABLE Pants (
                       product_id BIGINT PRIMARY KEY,
                       length VARCHAR(30) NOT NULL,
                       CONSTRAINT CK_length CHECK (length IN ('Shorts', 'Regular')),
                       FOREIGN KEY (product_id) REFERENCES Product(product_id)
);

-- Create the Top table
CREATE TABLE Top (
                     product_id BIGINT PRIMARY KEY,
                     sleevesType VARCHAR(30) NOT NULL,
                     CONSTRAINT CK_sleevesType CHECK (sleevesType IN ('T-shirt', 'Sweater')),
                     FOREIGN KEY (product_id) REFERENCES Product(product_id)
);

-- Create the Content table
CREATE TABLE Content (
                         product_id BIGINT NOT NULL,
                         order_id BIGINT NOT NULL,
                         quantity_ordered INTEGER NOT NULL,
                         PRIMARY KEY (product_id, order_id),
                         CONSTRAINT FK_product_id
                             FOREIGN KEY (product_id) REFERENCES Product(product_id),
                         CONSTRAINT FK_order_id_content
                             FOREIGN KEY (order_id) REFERENCES Order_record(order_id)
);

-- Insert customers into the Customer table
INSERT INTO Customer (first_name, last_name, email, address, phone_number, login_name, user_password)
VALUES
    ('Julien', 'Leclerc', 'julien.leclerc@gmail.com', '30 Avenue de Paris, Paris', '0637483990', 'juju', 'jok'),
    ('Louis', 'Jean', 'louis.jean@gmail.com', '30 Avenue du Marais, Paris', '0974663790', 'louis', 'lojean'),
    ('Marie', 'Dupont', 'marie.dupont@gmail.com', '15 Rue de Lyon, Lyon', '0647896541', 'Marie', 'jojd'),
    ('Pierre', 'Martin', 'pierre.martin@gmail.com', '10 Rue de Lille, Lille', '0623987654', 'PiJ', 'hdh');

-- Insert products into the Product table
INSERT INTO Product (name, price, stock, gender, color, size, description)
VALUES
    ('Sweatshirt', 40, 20, 'Unisex', 'Grey', 38, 'A comfortable sweatshirt'),
    ('Chino Pants', 60, 15, 'Male', 'Beige', 34, 'Elegant chino pants'),
    ('Blouse', 35, 25, 'Female', 'White', 36, 'Light cotton blouse'),
    ('Cargo Pants', 55, 10, 'Unisex', 'Black', 38, 'Practical cargo pants'),
    ('Hoodie', 45, 30, 'Unisex', 'Navy Blue', 40, 'Warm and cozy hoodie'),
    ('Formal Shirt', 50, 20, 'Male', 'Blue', 42, 'Elegant formal shirt');
    ('T-Shirt', 25, 50, 'Unisex', 'Black', 38, 'Basic cotton t-shirt'),
    ('Tank Top', 20, 40, 'Female', 'Pink', 34, 'Sleeveless tank top for summer'),
    ('Polo Shirt', 35, 25, 'Male', 'Green', 40, 'Classic short-sleeved polo shirt'),
    ('Cardigan', 60, 18, 'Female', 'Orange', 36, 'Soft and stylish cardigan'),
    ('Sweater', 50, 22, 'Male', 'Yellow', 40, 'Warm and cozy sweater'),
    ('Jeans', 70, 30, 'Unisex', 'Dark Blue', 38, 'Classic denim jeans'),
    ('Leggings', 30, 40, 'Female', 'Black', 36, 'Stretchable and comfortable leggings'),
    ('Shorts', 25, 35, 'Male', 'Green', 34, 'Casual summer shorts'),
    ('Track Pants', 40, 28, 'Unisex', 'Grey', 38, 'Perfect for workouts'),
    ('Pleated Pants', 65, 15, 'Female', 'Navy Blue', 36, 'Elegant pleated pants'),
    ('Joggers', 45, 20, 'Unisex', 'Olive Green', 40, 'Comfortable jogger pants');

-- Insert pants into the Pants table with specific attributes
INSERT INTO Pants (product_id, length)
VALUES
    (2, 'Regular'),  -- Chino Pants
    (4, 'Shorts');  -- Cargo Pants

-- Insert tops into the Top table with specific attributes
INSERT INTO Top (product_id, sleevesType)
VALUES
    (1, 'Sweater'),  -- Sweatshirt
    (3, 'T-shirt'),  -- Blouse
    (5, 'Sweater'),  -- Hoodie
    (6, 'T-shirt');   -- Formal Shirt

-- Insert orders into the Order_record table
INSERT INTO Order_record (customer_id, total_price, order_date, order_state)
VALUES
    (1, 1350, '2024-10-15', 'confirmed'),
    (2, 1000, '2024-10-16', 'in progress'),
    (3, 800, '2024-10-14', 'delivered'),
    (4, 1500, '2024-10-12', 'delivered');

-- Insert invoices into the Invoice table
INSERT INTO Invoice (order_id, invoice_date)
VALUES
    (1, '2024-10-16'),
    (2, '2024-10-17'),
    (3, '2024-10-15'),
    (4, '2024-10-13');

-- Insert contents into the Content table (products ordered in each order)
INSERT INTO Content (product_id, order_id, quantity_ordered)
VALUES
    (1, 1, 1),
    (4, 1, 1),
    (2, 2, 1),
    (3, 3, 1);
