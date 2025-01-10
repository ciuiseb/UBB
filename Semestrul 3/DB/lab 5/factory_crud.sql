CREATE PROCEDURE sp_InsertFactory
    @name VARCHAR(100),
    @adress VARCHAR(200),
    @number_of_employees INT
AS
BEGIN
    IF @number_of_employees < 0
        THROW 50010, 'Number of employees cannot be negative', 1;
        
    INSERT INTO Factories (name, adress, number_of_employees)
    VALUES (@name, @adress, @number_of_employees);
END;
GO

CREATE PROCEDURE sp_UpdateFactory
    @id INT,
    @name VARCHAR(100) = NULL,
    @adress VARCHAR(200) = NULL,
    @number_of_employees INT = NULL
AS
BEGIN
    IF @number_of_employees < 0
        THROW 50010, 'Number of employees cannot be negative', 1;
        
    UPDATE Factories
    SET name = ISNULL(@name, name),
        adress = ISNULL(@adress, adress),
        number_of_employees = ISNULL(@number_of_employees, number_of_employees)
    WHERE id = @id;
END;
GO

CREATE PROCEDURE sp_DeleteFactory
    @id INT
AS
BEGIN
    IF EXISTS (SELECT 1 FROM Products WHERE factory_id = @id)
        THROW 50011, 'Cannot delete factory with associated products', 1;
        
    IF EXISTS (SELECT 1 FROM ClientsFactories WHERE factory_id = @id)
        THROW 50012, 'Cannot delete factory with client associations', 1;
        
    DELETE FROM Factories WHERE id = @id;
END;
GO

CREATE PROCEDURE sp_GetFactory
    @id INT
AS
BEGIN
    SELECT * FROM Factories WHERE id = @id;
END;
GO

Select * from Factories
-- Tests
EXEC sp_InsertFactory
    @name = 'Test Factory',
    @adress = '123 Test Street',
    @number_of_employees = 100;

-- 25
EXEC sp_GetFactory @id = 26;

EXEC sp_UpdateFactory
    @id = 26,
    @name = 'Updated Factory',
    @number_of_employees = 150;

-- Get Updated Factory
EXEC sp_GetFactory @id = 26;

-- Delete Factory
EXEC sp_DeleteFactory @id = 26;

-- Verify Delete (should return no rows)
EXEC sp_GetFactory @id = 26;
GO