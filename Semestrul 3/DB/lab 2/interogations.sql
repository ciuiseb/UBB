USE [LAB1];

-- Names of clients who reviewed products from Factory with given id
SELECT DISTINCT c.name
FROM Clients c
JOIN ProductReviews pr ON c.id = pr.client_id
JOIN Products p ON p.id = pr.product_id
JOIN Factories f ON f.id = p.factory_id
WHERE f.id = 2;

-- Products with an rating greater than 3 reviewd by client with a given mail
SELECT p.name, pr.review_date
FROM Products p
JOIN ProductReviews pr ON pr.product_id = p.id
JOIN Clients c ON c.id = pr.client_id
WHERE c.mail = 'info@techstore.ro'
AND pr.rating > 3;
-- Distinct product names reviewed by clients who rated them with a 5
SELECT DISTINCT p.name, c.name, pr.review_date
FROM Products p
JOIN ProductReviews pr ON pr.product_id = p.id
JOIN Clients c ON c.id = pr.client_id
WHERE pr.rating = 5;
-- Number of departments for each factory with more than 5 employees
SELECT d.name, COUNT(e.id) as number_of_engineers
FROM Departments d
LEFT JOIN Engineers e ON e.department_id = d.id
GROUP BY d.name 
HAVING COUNT(e.id) > 5;
-- Names of engineers working in departments located on the first floor
SELECT e.name, d.name
FROM Engineers e
JOIN Departments d ON e.department_id = d.id 
WHERE d.floor = 1;
--No. of engineers and products worked on for each department
SELECT d.name AS department_name, COUNT(DISTINCT e.id) AS engineer_count,  COUNT(DISTINCT p.id) AS product_count
FROM Departments d
JOIN Engineers e ON d.id = e.department_id
JOIN ProductEngineers pe ON e.id = pe.engineer_id
JOIN Products p ON pe.product_id = p.id
GROUP BY d.id, d.name;
-- Get materials needed for products with average stock < 200
SELECT m.name, COUNT(DISTINCT p.id) AS product_count
FROM Materials m
JOIN ProductsComposition pc ON m.id = pc.material_id
JOIN Products p ON pc.product_id = p.id
GROUP BY m.id, m.name
HAVING AVG(p.stock) < 200;
-- Names of products that use a given material
SELECT p.name
FROM Products p
JOIN ProductsComposition pc ON p.id = pc.product_id
JOIN Materials m ON pc.material_id = m.id
WHERE m.name = 'Copper';
-- Factories with more than 3 reviews for their products
SELECT f.name AS factory_name, COUNT(pr.id) AS review_count
FROM Factories f
JOIN Products p ON f.id = p.factory_id
JOIN  ProductReviews pr ON p.id = pr.product_id
GROUP BY f.name HAVING COUNT(pr.id) > 3;
-- Show engineers, their departments, and salary
SELECT e.name as engineer_name, d.name as department_name, e.monthly_salary
FROM Engineers e
JOIN Departments d ON e.department_id = d.id
JOIN ProductEngineers pe ON e.id = pe.engineer_id
GROUP BY e.name, d.name, e.monthly_salary;