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
    ('Hoodie', 45, 30, 'Unisex', 'Blue', 40, 'Warm and cozy hoodie'),
    ('Formal Shirt', 50, 20, 'Male', 'Blue', 42, 'Elegant formal shirt'),
    ('T-Shirt', 25, 50, 'Unisex', 'Black', 38, 'Basic cotton t-shirt'),
    ('Tank Top', 20, 40, 'Female', 'Pink', 34, 'Sleeveless tank top for summer'),
    ('Polo Shirt', 35, 25, 'Male', 'Green', 40, 'Classic short-sleeved polo shirt'),
    ('Cardigan', 60, 18, 'Female', 'Orange', 36, 'Soft and stylish cardigan'),
    ('Sweater', 50, 22, 'Male', 'Yellow', 40, 'Warm and cozy sweater'),
    ('Jeans', 70, 30, 'Unisex', 'Blue', 38, 'Classic denim jeans'),
    ('Leggings', 30, 40, 'Female', 'Black', 36, 'Stretchable and comfortable leggings'),
    ('Shorts', 25, 35, 'Male', 'Green', 34, 'Casual summer shorts'),
    ('Jogging', 40, 28, 'Unisex', 'Grey', 38, 'Perfect for workouts'),
    ('Pleated Pants', 65, 15, 'Female', 'White', 36, 'Elegant pleated pants'),
    ('Jogging', 45, 20, 'Unisex', 'Green', 40, 'Comfortable jogger pants'),
    ('Sweatshirt', 40, 25, 'Unisex', 'Red', 38, 'Cozy sweatshirt with a minimalist design'),
    ('Hoodie', 50, 30, 'Unisex', 'Yellow', 40, 'Bright hoodie perfect for casual outings'),
    ('Tank Top', 18, 35, 'Female', 'Green', 34, 'Lightweight tank top for summer'),
    ('Cardigan', 70, 15, 'Female', 'Red', 36, 'Stylish cardigan with button closure'),
    ('T-Shirt', 22, 50, 'Unisex', 'Blue', 38, 'Soft and breathable cotton t-shirt'),
    ('Polo Shirt', 40, 28, 'Male', 'Pink', 40, 'Comfortable polo shirt with short sleeves'),
    ('Sweater', 55, 18, 'Male', 'Grey', 40, 'Warm knitted sweater for winter'),
    ('Blouse', 38, 20, 'Female', 'Yellow', 36, 'Elegant blouse with subtle patterns'),
    ('Jeans', 75, 35, 'Unisex', 'Blue', 38, 'Skinny fit denim jeans'),
    ('Chino Pants', 65, 25, 'Male', 'Green', 34, 'Slim-fit chino pants for a casual look'),
    ('Leggings', 28, 40, 'Female', 'Pink', 36, 'Comfortable and flexible leggings'),
    ('Shorts', 30, 30, 'Male', 'Grey', 34, 'Relaxed-fit shorts for outdoor activities'),
    ('Jogging', 45, 20, 'Unisex', 'White', 40, 'Stylish jogger pants with elastic cuffs'),
    ('Pleated Pants', 70, 10, 'Female', 'Orange', 36, 'Elegant pleated pants for formal occasions'),
    ('Jogging', 50, 22, 'Unisex', 'Red', 38, 'Sporty track pants for exercise'),
    ('Denim', 80, 15, 'Unisex', 'Black', 38, 'Wide-leg denim pants with a vintage vibe'),
    ('Hooded Sweatshirt', 48, 25, 'Unisex', 'Green', 38, 'Stylish hoodie with kangaroo pockets'),
    ('Tunic', 50, 30, 'Female', 'Red', 36, 'Flowy tunic perfect for summer'),
    ('Crop Top', 25, 40, 'Female', 'Pink', 34, 'Trendy crop top for casual wear'),
    ('Oversized T-Shirt', 35, 20, 'Unisex', 'Orange', 40, 'Comfortable oversized t-shirt'),
    ('Wool Sweater', 60, 15, 'Female', 'Yellow', 40, 'Premium wool sweater for cold weather'),
    ('Turtle Neck', 45, 20, 'Female', 'Blue', 36, 'Stylish turtle neck top for colder days'),
    ('Party Top', 55, 15, 'Female', 'Black', 36, 'Elegant top for evening events'),
    ('Zip Sweatshirt', 50, 25, 'Male', 'White', 40, 'Comfortable sweatshirt with a zip closure'),
    ('Long Sleeve Top', 30, 30, 'Female', 'Pink', 36, 'Soft long-sleeved top for everyday wear'),
    ('Flared Pants', 60, 18, 'Female', 'Red', 38, 'Fashionable flared pants with a retro vibe'),
    ('Oversized Jeans', 75, 15, 'Male', 'Grey', 38, 'Comfortable oversized denim jeans'),
    ('Military Shorts', 40, 20, 'Female', 'Green', 34, 'Casual military-inspired shorts'),
    ('Fleece Shorts', 35, 25, 'Male', 'Yellow', 36, 'Cozy fleece shorts for relaxation'),
    ('Wool Sweater', 65, 12, 'Male', 'White', 42, 'Warm wool sweater for winter days'),
    ('Lace Top', 50, 18, 'Female', 'Black', 36, 'Elegant lace top for special occasions'),
    ('Sequined Top', 70, 10, 'Female', 'Yellow', 34, 'Sparkling sequined top for parties'),
    ('Tank Top', 25, 40, 'Male', 'Orange', 38, 'Light and breathable tank top for summer'),
    ('Turtleneck', 50, 20, 'Male', 'Black', 40, 'Classic turtleneck sweater for colder seasons'),
    ('T-Shirt', 28, 50, 'Male', 'Green', 38, 'Simple and comfortable t-shirt'),
    ('Flared Pants', 65, 15, 'Female', 'Black', 36, 'Elegant and stylish flared pants'),
    ('Shirt', 55, 20, 'Male', 'Blue', 42, 'Smart and versatile shirt'),
    ('Oversized T-Shirt', 40, 20, 'Male', 'Red', 40, 'Casual oversized t-shirt'),
    ('Oversized T-Shirt', 45, 18, 'Unisex', 'White', 40, 'Comfortable and trendy oversized t-shirt'),
    ('Oversized Jeans', 80, 12, 'Unisex', 'Blue', 38, 'Relaxed-fit oversized denim jeans'),
    ('Cargo Shorts', 35, 25, 'Male', 'Pink', 36, 'Bright and functional cargo shorts'),
    ('Military Cargo', 50, 20, 'Unisex', 'Green', 38, 'Durable cargo pants with a military design'),
    ('Blouse', 40, 22, 'Female', 'Pink', 36, 'Chic blouse with a flattering fit'),
    ('Turtle Neck', 48, 18, 'Male', 'Grey', 40, 'Cozy turtle neck sweater for cooler weather');  

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
