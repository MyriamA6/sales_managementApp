DROP TABLE IF EXISTS Content;
DROP TABLE IF EXISTS Pants;
DROP TABLE IF EXISTS Top;
DROP TABLE IF EXISTS Product;
DROP TABLE IF EXISTS Invoice;
DROP TABLE IF EXISTS Order_record;
DROP TABLE IF EXISTS Customer;

CREATE TABLE Customer (
                          customer_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          first_name VARCHAR(50) NOT NULL,
                          last_name VARCHAR(50) NOT NULL,
                          email VARCHAR(100) NOT NULL,
                          address VARCHAR(100) NOT NULL,
                          phone_number VARCHAR(20),
                          login_name VARCHAR(30) NOT NULL,
                          user_password VARCHAR(256) NOT NULL
);

CREATE TABLE Order_record (
                              order_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              customer_id BIGINT NOT NULL,
                              total_price INT NOT NULL,
                              order_date DATE,
                              order_state VARCHAR(50) NOT NULL,
                              CONSTRAINT FK_Customer_Order_record
                                  FOREIGN KEY (customer_id) REFERENCES Customer(customer_id),
                              CONSTRAINT CK_order_state
                                  CHECK (order_state IN ('in progress', 'paid', 'delivered'))
);

CREATE TABLE Invoice (
                         invoice_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         order_id BIGINT UNIQUE NOT NULL,
                         invoice_date DATE,
                         CONSTRAINT FK_order_id
                             FOREIGN KEY (order_id) REFERENCES Order_record(order_id)
);

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

CREATE TABLE Pants (
                       product_id BIGINT UNIQUE PRIMARY KEY,
                       length VARCHAR(30) NOT NULL,
                       CONSTRAINT CK_length CHECK (length IN ('Shorts', 'Regular')),
                       CONSTRAINT FK_Pants_Product FOREIGN KEY (product_id) REFERENCES Product(product_id)
);

CREATE TABLE Top (
                     product_id BIGINT UNIQUE PRIMARY KEY,
                     sleevesType VARCHAR(30) NOT NULL,
                     CONSTRAINT CK_sleevesType CHECK (sleevesType IN ('T-shirt', 'Sweater')),
                     CONSTRAINT FK_Top_Product FOREIGN KEY (product_id) REFERENCES Product(product_id)
);

CREATE TABLE Content (
                         product_id BIGINT NOT NULL,
                         order_id BIGINT NOT NULL,
                         quantity_ordered INT NOT NULL,
                         PRIMARY KEY (product_id, order_id),
                         CONSTRAINT FK_Content_Product FOREIGN KEY (product_id) REFERENCES Product(product_id),
                         CONSTRAINT FK_Content_Order FOREIGN KEY (order_id) REFERENCES Order_record(order_id)
);

-- Insertions for the customers
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

-- Insertion into Product table
INSERT INTO Product (name, price, stock, gender, color, size, description)
VALUES
    ('Sweatshirt', 40, 2, 'Unisex', 'Grey', 38, 'A comfortable sweatshirt'),
    ('Chino Pants', 60, 15, 'Male', 'Yellow', 34, 'Elegant chino pants'),
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
    ('Skinny Jeans', 75, 35, 'Unisex', 'Blue', 38, 'Skinny fit denim jeans'),
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
    ('Turtleneck', 45, 20, 'Female', 'Blue', 36, 'Stylish turtle neck top for colder days'),
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
    ('Turtleneck', 48, 18, 'Male', 'Grey', 40, 'Cozy turtle neck sweater for cooler weather'),
    ('Sweatshirt', 40, 20, 'Unisex', 'Grey', 34, 'A comfortable sweatshirt'),
    ('Chino Pants', 60, 15, 'Male', 'Yellow', 36, 'Elegant chino pants'),
    ('Blouse', 35, 25, 'Female', 'White', 34, 'Light cotton blouse'),
    ('Cargo Pants', 55, 10, 'Unisex', 'Black', 40, 'Practical cargo pants'),
    ('Hoodie', 45, 30, 'Unisex', 'Blue', 42, 'Warm and cozy hoodie'),
    ('Formal Shirt', 50, 20, 'Male', 'Blue', 40, 'Elegant formal shirt'),
    ('T-Shirt', 25, 50, 'Unisex', 'Black', 34, 'Basic cotton t-shirt'),
    ('T-Shirt', 25, 50, 'Unisex', 'Black', 40, 'Basic cotton t-shirt'),
    ('Tank Top', 25, 40, 'Male', 'Orange', 34, 'Light and breathable tank top for summer'),
    ('Tank Top', 25, 40, 'Male', 'Orange', 36, 'Light and breathable tank top for summer'),
    ('Polo Shirt', 35, 25, 'Male', 'Green', 34, 'Classic short-sleeved polo shirt'),
    ('Polo Shirt', 35, 25, 'Male', 'Green', 36, 'Classic short-sleeved polo shirt'),
    ('Polo Shirt', 35, 25, 'Male', 'Green', 42, 'Classic short-sleeved polo shirt'),
    ('Cardigan', 60, 18, 'Female', 'Orange', 34, 'Soft and stylish cardigan'),
    ('Cardigan', 60, 18, 'Female', 'Orange', 40, 'Soft and stylish cardigan'),
    ('Cardigan', 60, 18, 'Female', 'Orange', 42, 'Soft and stylish cardigan'),
    ('Sweater', 50, 22, 'Male', 'Yellow', 34, 'Warm and cozy sweater'),
    ('Sweater', 50, 22, 'Male', 'Yellow', 36, 'Warm and cozy sweater'),
    ('Sweater', 50, 22, 'Male', 'Yellow', 38, 'Warm and cozy sweater'),
    ('Sweater', 50, 22, 'Male', 'Yellow', 42, 'Warm and cozy sweater'),
    ('Jeans', 70, 30, 'Unisex', 'Blue', 34, 'Classic denim jeans'),
    ('Jeans', 70, 30, 'Unisex', 'Blue', 36, 'Classic denim jeans'),
    ('Jeans', 70, 30, 'Unisex', 'Blue', 40, 'Classic denim jeans'),
    ('Jeans', 70, 30, 'Unisex', 'Blue', 42, 'Classic denim jeans'),
    ('Leggings', 30, 40, 'Female', 'Black', 34, 'Stretchable and comfortable leggings'),
    ('Leggings', 30, 40, 'Female', 'Black', 38, 'Stretchable and comfortable leggings'),
    ('Leggings', 30, 40, 'Female', 'Black', 40, 'Stretchable and comfortable leggings'),
    ('Leggings', 30, 40, 'Female', 'Black', 42, 'Stretchable and comfortable leggings'),
    ('Shorts', 25, 20, 'Male', 'Green', 36, 'Casual summer shorts'),
    ('Shorts', 25, 20, 'Male', 'Green', 40, 'Casual summer shorts'),
    ('Pleated Pants', 65, 10, 'Female', 'White', 38, 'Elegant pleated pants'),
    ('Pleated Pants', 65, 10, 'Female', 'White', 40, 'Elegant pleated pants'),
    ('Jogging', 45, 25, 'Unisex', 'White', 36, 'Stylish jogger pants with elastic cuffs'),
    ('Jogging', 45, 25, 'Unisex', 'White', 42, 'Stylish jogger pants with elastic cuffs'),
    ('Hoodie', 50, 30, 'Unisex', 'Yellow', 36, 'Bright hoodie perfect for casual outings'),
    ('Hoodie', 50, 30, 'Unisex', 'Yellow', 42, 'Bright hoodie perfect for casual outings'),
    ('Tank Top', 20, 30, 'Female', 'Pink', 36, 'Sleeveless tank top for summer'),
    ('Tank Top', 20, 30, 'Female', 'Pink', 38, 'Sleeveless tank top for summer'),
    ('Tank Top', 20, 30, 'Female', 'Pink', 40, 'Sleeveless tank top for summer'),
    ('Oversized Jeans', 80, 15, 'Unisex', 'Blue', 36, 'Relaxed-fit oversized denim jeans'),
    ('Oversized Jeans', 80, 15, 'Unisex', 'Blue', 40, 'Relaxed-fit oversized denim jeans'),
    ('Oversized T-Shirt', 35, 18, 'Unisex', 'Orange', 36, 'Comfortable oversized t-shirt'),
    ('Oversized T-Shirt', 35, 18, 'Unisex', 'Orange', 38, 'Comfortable oversized t-shirt'),
    ('Oversized T-Shirt', 35, 18, 'Unisex', 'Orange', 42, 'Comfortable oversized t-shirt'),
    ('Denim', 80, 10, 'Unisex', 'Black', 36, 'Wide-leg denim pants with a vintage vibe'),
    ('Denim', 80, 10, 'Unisex', 'Black', 42, 'Wide-leg denim pants with a vintage vibe'),
    ('T-Shirt', 28, 40, 'Male', 'Green', 36, 'Simple and comfortable t-shirt'),
    ('T-Shirt', 28, 40, 'Male', 'Green', 40, 'Simple and comfortable t-shirt'),
    ('T-Shirt', 28, 40, 'Male', 'Green', 42, 'Simple and comfortable t-shirt'),
    ('Tunic', 50, 20, 'Female', 'Red', 38, 'Flowy tunic perfect for summer'),
    ('Tunic', 50, 20, 'Female', 'Red', 42, 'Flowy tunic perfect for summer'),
    ('Zip Sweatshirt', 50, 18, 'Male', 'White', 38, 'Comfortable sweatshirt with a zip closure'),
    ('Zip Sweatshirt', 50, 18, 'Male', 'White', 42, 'Comfortable sweatshirt with a zip closure'),
    ('Military Cargo', 50, 20, 'Unisex', 'Green', 36, 'Durable cargo pants with a military design'),
    ('Military Cargo', 50, 20, 'Unisex', 'Green', 40, 'Durable cargo pants with a military design'),
    ('Fleece Shorts', 35, 20, 'Male', 'Yellow', 38, 'Cozy fleece shorts for relaxation'),
    ('Fleece Shorts', 35, 20, 'Male', 'Yellow', 40, 'Cozy fleece shorts for relaxation'),
    ('Turtleneck', 50, 20, 'Male', 'Black', 36, 'Classic turtleneck sweater for colder seasons'),
    ('Turtleneck', 50, 20, 'Male', 'Black', 38, 'Classic turtleneck sweater for colder seasons'),
    ('Turtleneck', 50, 20, 'Male', 'Black', 42, 'Classic turtleneck sweater for colder seasons'),
    ('Flared Pants', 65, 15, 'Female', 'Black', 38, 'Elegant and stylish flared pants'),
    ('Flared Pants', 65, 15, 'Female', 'Black', 40, 'Elegant and stylish flared pants'),
    ('Flared Pants', 65, 15, 'Female', 'Black', 42, 'Elegant and stylish flared pants');

-- Insertion of the pants
INSERT INTO Pants (product_id, length)
VALUES
    (2, 'Regular'), -- Chino Pants
    (4, 'Regular'), -- Cargo Pants
    (12, 'Regular'), -- Jeans
    (13, 'Regular'), -- Leggins
    (14, 'Shorts'), -- Shorts
    (15, 'Regular'), -- Jogging
    (16, 'Regular'), -- Pleated Pants
    (17, 'Regular'), -- Jogging
    (26, 'Regular'), -- Jeans
    (27, 'Regular'), -- Chino Pants
    (28, 'Regular'), -- Leggings
    (29, 'Shorts'), -- Shorts
    (30, 'Regular'), -- Jogging
    (31, 'Regular'), -- Pleated Pants
    (32, 'Regular'), -- Jogging
    (33, 'Regular'), -- Denim
    (43, 'Regular'), -- Flared Pants
    (44, 'Regular'), -- Oversized Jeans
    (45, 'Shorts'), -- Military Shorts
    (46, 'Shorts'), -- Fleece Shorts
    (53, 'Regular'), -- Flared Pants
    (57, 'Regular'), -- Oversized Jeans
    (58, 'Shorts'), -- Cargo Shorts
    (59, 'Regular'), -- Military Cargo
    (63, 'Regular'), -- Chino Pants
    (65, 'Regular'), -- Cargo Pants
    (82, 'Regular'), -- Jeans
    (83, 'Regular'), -- Jeans
    (84, 'Regular'), -- Jeans
    (85, 'Regular'), -- Jeans
    (86, 'Regular'), -- Leggings
    (87, 'Regular'), -- Leggings
    (88,'Regular' ), -- Leggings
    (89, 'Regular'), -- Leggings
    (90, 'Shorts'), -- Shorts
    (91, 'Shorts'), -- Shorts
    (92, 'Regular'), -- Pleated Pants
    (93, 'Regular'), -- Pleated Pants
    (94, 'Regular'), -- Jogging
    (95, 'Regular'), -- Jogging
    (101, 'Regular'), -- Oversized Jeans
    (102, 'Regular'), -- Oversized Jeans
    (106, 'Regular'), -- Denim
    (107, 'Regular'), -- Denim
    (115, 'Regular'), -- Military Cargo
    (116, 'Regular'), -- Military Cargo
    (117, 'Shorts'), -- Fleece Shorts
    (118, 'Shorts'), -- Fleece Shorts
    (122, 'Regular'), -- Flared Pants
    (123, 'Regular'), -- Flared Pants
    (124, 'Regular'); -- Flared Pants


-- Insertion of the tops
INSERT INTO Top (product_id, sleevesType)
VALUES
    (1, 'Sweater'), -- Sweatshirt
    (3, 'T-shirt'), -- Blouse
    (5, 'Sweater'), -- Hoodie
    (6, 'T-shirt'), -- Formal Shirt
    (7, 'T-shirt'), -- T-Shirt
    (8, 'T-shirt'), -- Tank Top
    (9, 'T-shirt'), -- Polo Shirt
    (10, 'Sweater'), -- Cardigan
    (11, 'Sweater'), -- Sweater
    (18, 'Sweater'), -- Sweatshirt
    (19, 'Sweater'), -- Hoodie
    (20, 'T-shirt'), -- Tank Top
    (21, 'Sweater'), -- Cardigan
    (22, 'T-shirt'), -- T-Shirt
    (23, 'T-shirt'), -- Polo Shirt
    (24, 'Sweater'), -- Sweater
    (25, 'T-shirt'), -- Blouse
    (34, 'Sweater'), -- Hooded Sweatshirt
    (35,'T-shirt' ), -- Tunic
    (36,'T-shirt' ), -- Crop Top
    (37, 'T-shirt'), -- Oversized T-Shirt
    (38, 'Sweater'), -- Wool Sweater
    (39, 'T-shirt'), -- Turtle Neck
    (40, 'T-shirt'), -- Party Top
    (41, 'Sweater'), -- Zip Sweatshirt
    (42, 'T-shirt'), -- Long Sleeve Top
    (47, 'Sweater'), -- Wool Sweater
    (48, 'T-shirt'), -- Lace Top
    (49, 'T-shirt'), -- Sequined Top
    (50, 'T-shirt'), -- Tank Top
    (51, 'T-shirt'), -- Turtle Neck
    (52, 'T-shirt'), -- T-Shirt
    (54, 'T-shirt'), -- Shirt
    (55, 'T-shirt'), -- Oversied T-Shirt
    (56, 'T-shirt'), -- Oversied T-Shirt
    (60, 'T-shirt'), -- Blouse
    (61, 'T-shirt'), -- Turtle Neck
    (62, 'Sweater'), -- Sweatshirt
    (64, 'T-shirt'), -- Blouse
    (66, 'Sweater'), -- Hoodie
    (67, 'T-shirt'), -- Formal Shirt
    (68, 'T-shirt'), -- T-Shirt
    (69, 'T-shirt'), -- T-Shirt
    (70, 'T-shirt'), -- Tank Top
    (71, 'T-shirt'), -- Tank Top
    (72, 'T-shirt'), -- Polo Shirt
    (73, 'T-shirt'), -- Polo Shirt
    (74, 'T-shirt'), -- Polo Shirt
    (75, 'Sweater'), -- Cardigan
    (76, 'Sweater'), -- Cardigan
    (77, 'Sweater'), -- Cardigan
    (78, 'Sweater'), --  Sweater
    (79, 'Sweater'), --  Sweater
    (80, 'Sweater'), --  Sweater
    (81, 'Sweater'), --  Sweater
    (96, 'Sweater'), -- Hoodie
    (97, 'Sweater'), -- Hoodie
    (98, 'T-shirt'), -- Tank Top
    (99, 'T-shirt'), -- Tank Top
    (100, 'T-shirt'), -- Tank Top
    (103, 'T-shirt'), -- Oversized T-Shirt
    (104, 'T-shirt'), -- Oversized T-Shirt
    (105, 'T-shirt'), -- Oversized T-Shirt
    (108, 'T-shirt'), -- T-Shirt
    (109, 'T-shirt'), -- T-Shirt
    (110, 'T-shirt'), -- T-Shirt
    (111, 'T-shirt'), -- Tunic
    (112, 'T-shirt'), -- Tunic
    (113, 'Sweater'), -- Zip Sweatshirt
    (114, 'Sweater'), -- Zip Sweatshirt
    (119, 'T-shirt'), -- Turtle Neck
    (120, 'T-shirt'), -- Turtle Neck
    (121, 'T-shirt'); -- Turtle Neck -- Formal Shirt

-- Insertion of the orders
INSERT INTO Order_record (customer_id, total_price, order_date, order_state)
VALUES
    (1, 95, DATE '2024-10-15', 'paid'),
    (2, 60, DATE '2024-10-16', 'in progress'),
    (3, 35, DATE '2024-10-14', 'delivered');

-- Insertion of the invoices
INSERT INTO Invoice (order_id, invoice_date)
VALUES
    (1, DATE '2024-10-16'),
    (3, DATE '2024-10-15');

-- Insertion of the content of the orders
INSERT INTO Content (product_id, order_id, quantity_ordered)
VALUES
    (1, 1, 1),
    (4, 1, 1),
    (2, 2, 1),
    (3, 3, 1);
