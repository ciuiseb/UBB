CREATE DATABASE LAB1;
USE [LAB1];
--creating the factories table; it will store only the name and the id
CREATE TABLE Factories (
	id INT IDENTITY(1,1), 
	name VARCHAR(100) NOT NULL,

	PRIMARY KEY(id)
);

--creating the products table; it will store the name, the factory at which it is produced as well as the id
CREATE TABLE Products (
	id INT IDENTITY(1,1),
	name VARCHAR(100) NOT NULL,
	factory_id INT,
	FOREIGN KEY (factory_id) REFERENCES Factories(id),
	PRIMARY KEY (id)
)

-- creating the materials table, which are needed for each product; they'll have id and name
CREATE TABLE Materials(
	id INT IDENTITY(1,1), 
	name VARCHAR(100) NOT NULL, 
	PRIMARY KEY(id)
)

-- creating the table ProductsComposition to connect the products each to their materials
CREATE TABLE ProductsComposition(
	product_id INT, 
	material_id INT, 
	PRIMARY KEY (product_id, material_id), 
	FOREIGN KEY (product_id) REFERENCES Products(id), 
	FOREIGN KEY (material_id) REFERENCES Materials(id)
)

-- creating the Clients table, which may represent individual shops, or corporations; they have a name and an id
CREATE TABLE Clients(
	id INT IDENTITY (1,1), 
	name VARCHAR (100), 
	PRIMARY KEY (id)
);

-- creaing the table ClientsFactories, to represent which client buys from which factory, 
-- as well as what factory delivers to what client

CREATE TABLE ClientsFactories(
	client_id INT NOT NULL, 
	factory_id INT NOT NULL,
	PRIMARY KEY (client_id,factory_id), 
	FOREIGN KEY (client_id) REFERENCES Clients(id),
	FOREIGN KEY (factory_id) REFERENCES Factories(id),
	
);


-- creating the engenieers table; they design, or have designed the products
CREATE TABLE Engineers(
	id INT IDENTITY(1,1),
	name VARCHAR(100) NOT NULL, 
	monthly_salary INT NOT NULL,
	PRIMARY KEY (id)
)

--creating the ProductEngineers table
CREATE TABLE ProductEngineers(
	product_id INT NOT NULL, 
	engineer_id INT NOT NULL, 
	PRIMARY KEY(product_id, engineer_id), 
	FOREIGN KEY (product_id) REFERENCES Products(id), 
	FOREIGN KEY (engineer_id) REFERENCES Engineers(id)
)

-- adding a review table
CREATE TABLE ProductReviews (
    id INT IDENTITY(1,1),
    product_id INT,
    client_id INT,
    rating INT CHECK (rating >= 1 AND rating <= 5),
    comment VARCHAR(100),
    review_date DATE DEFAULT getdate(),
    PRIMARY KEY (id),
    FOREIGN KEY (product_id) REFERENCES Products(id),
    FOREIGN KEY (client_id) REFERENCES Clients(id)
);

-- moving stock and the update date to products table itself
ALTER TABLE Products
ADD stock int;
ALTER TABLE Products
ADD last_updated DATE DEFAULT getdate();

-- deleteing the inventory table
DROP TABLE Inventory;

-- creating a new table for departments
CREATE TABLE Departments(
	id INT IDENTITY(1,1), 
	name VARCHAR(100) NOT NULL, 
	floor INT CHECK (floor >=0 and floor <= 9), 
	room_number INT CHECK (room_number >= 1 and room_number <= 999),
	PRIMARY KEY (id), 
)

-- giving each engineer a department
ALTER TABLE Engineers
ADD department_id INT;
ALTER TABLE Engineers
ADD FOREIGN KEY (department_id) REFERENCES Departments(id);

-- adding some new attributes for clients
ALTER TABLE Clients
ADD phone_number CHAR(10);
ALTER TABLE Clients
ADD mail VARCHAR(100);

--adding some new attributes for factories
ALTER TABLE Factories
ADD adress VARCHAR(100);
ALTER TABLE Factories
ADD number_of_employees INT;
