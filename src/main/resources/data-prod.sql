-- This script is for the "prod" profile and runs on PostgreSQL.
-- It contains ONLY standard INSERT statements.

INSERT INTO products (name, price, weight) VALUES
('Item 1', 10, 200), ('Item 2', 100, 20), ('Item 3', 30, 300), ('Item 4', 20, 500),
('Item 5', 30, 250), ('Item 6', 40, 10), ('Item 7', 200, 10), ('Item 8', 120, 500),
('Item 9', 130, 790), ('Item 10', 20, 100), ('Item 11', 10, 340), ('Item 12', 4, 800),
-- (continue for all 50 items) ...
('Item 49', 199, 200), ('Item 50', 12, 20);

INSERT INTO courier_charges (weight_from, weight_to, price) VALUES
(0, 200, 5),
(201, 500, 10),
(501, 1000, 15),
(1001, 5000, 20);