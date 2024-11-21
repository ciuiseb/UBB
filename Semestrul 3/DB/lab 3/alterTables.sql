USE [LAB3];

-- alter column type
ALTER TABLE Engineers
ALTER COLUMN monthly_salary DECIMAL(10,2); 

-- default constraint
ALTER TABLE Engineers
ADD CONSTRAINT DF_Engineers_monthly_salary 
DEFAULT 1600.00 FOR monthly_salary;

-- create a table
CREATE TABLE NewDepartment (
    id INT PRIMARY KEY,
    name VARCHAR(100),
    location VARCHAR(200)
);
-- drop a table
--DROP TABLE ProductReviews;

-- new column
ALTER TABLE Factories
ADD manager VARCHAR(200);

-- new foreign key
ALTER TABLE ProductsComposition
ADD CONSTRAINT FK_ProductComposition_Materials
FOREIGN KEY (product_id) REFERENCES Materials(id);

-- drop foreign key
--ALTER TABLE ProductsComposition
--DROP CONSTRAINT FK_ProductComposition_Materials;