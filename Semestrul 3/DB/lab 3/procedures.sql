USE [LAB3]
GO
CREATE PROCEDURE proc_ChangeEngineerSalaryToDecimal
AS
BEGIN
   BEGIN
       ALTER TABLE Engineers ALTER COLUMN monthly_salary DECIMAL(10,2);
       INSERT INTO DatabaseVersions (version_id, description)
       VALUES (1, 'Modified Engineers.monthly_salary to DECIMAL(10,2)');
   END
END;
GO

CREATE PROCEDURE proc_AddDefaultStockToProducts 
AS
BEGIN
   BEGIN
       ALTER TABLE Products 
       ADD CONSTRAINT DF_Products_Stock DEFAULT 0 FOR stock;
       INSERT INTO DatabaseVersions (version_id, description)
       VALUES (2, 'Added default constraint of 0 for Products.stock');
   END
END;
GO

CREATE PROCEDURE proc_CreateNewDepartmentTable
AS
BEGIN
   BEGIN
       CREATE TABLE NewDepartment (
           id INT PRIMARY KEY,
           name VARCHAR(100),
           location VARCHAR(200)
       );
       INSERT INTO DatabaseVersions (version_id, description)
       VALUES (3, 'Created NewDepartment table');
   END
END;
GO

CREATE PROCEDURE proc_AddManagerToFactories
AS
BEGIN
   BEGIN
       ALTER TABLE Factories ADD manager VARCHAR(200);
       INSERT INTO DatabaseVersions (version_id, description)
       VALUES (4, 'Added manager column to Factories table');
   END
END;
GO

CREATE PROCEDURE proc_AddMaterialsFKToProducts
AS
BEGIN
   BEGIN
       ALTER TABLE ProductsComposition 
       ADD CONSTRAINT FK_ProductComposition_Materials
       FOREIGN KEY (product_id) REFERENCES Materials(id);
       INSERT INTO DatabaseVersions (version_id, description)
       VALUES (5, 'Added foreign key constraint to ProductsComposition');
   END
END;
GO
-- Undos
CREATE PROCEDURE proc_UndoEngineerSalaryToInt
AS
BEGIN
    ALTER TABLE Engineers ALTER COLUMN monthly_salary INT;
    DELETE FROM DatabaseVersions WHERE version_id = 1;
END;
GO

CREATE PROCEDURE proc_RemoveDefaultStockFromProducts
AS
BEGIN
    ALTER TABLE Products DROP CONSTRAINT DF_Products_Stock;
    DELETE FROM DatabaseVersions WHERE version_id = 2;
END;
GO

CREATE PROCEDURE proc_DropNewDepartmentTable
AS
BEGIN
    DROP TABLE NewDepartment;
    DELETE FROM DatabaseVersions WHERE version_id = 3;
END;
GO

CREATE PROCEDURE proc_RemoveManagerFromFactories
AS
BEGIN
    ALTER TABLE Factories DROP COLUMN manager;
    DELETE FROM DatabaseVersions WHERE version_id = 4;
END;
GO

CREATE PROCEDURE proc_RemoveMaterialsFKFromProducts
AS
BEGIN
    ALTER TABLE ProductsComposition DROP CONSTRAINT FK_ProductComposition_Materials;
    DELETE FROM DatabaseVersions WHERE version_id = 5;
END;
GO

DROP PROCEDURE proc_ChangeEngineerSalaryToDecimal;
DROP PROCEDURE proc_AddDefaultStockToProducts;
DROP PROCEDURE proc_CreateNewDepartmentTable;
DROP PROCEDURE proc_AddManagerToFactories;
DROP PROCEDURE proc_AddMaterialsFKToProducts;
DROP PROCEDURE proc_UndoEngineerSalaryToInt;
DROP PROCEDURE proc_RemoveDefaultStockFromProducts;
DROP PROCEDURE proc_DropNewDepartmentTable;
DROP PROCEDURE proc_RemoveManagerFromFactories;
DROP PROCEDURE proc_RemoveMaterialsFKFromProducts;