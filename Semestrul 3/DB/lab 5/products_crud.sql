CREATE PROCEDURE sp_InsertProduct
    @name VARCHAR(100),
    @factory_id INT,
    @stock INT,
    @last_updated DATETIME
AS
BEGIN
    IF @stock < 0
        THROW 50001, 'Stock cannot be negative', 1;
    
    IF NOT EXISTS (SELECT 1 FROM Factories WHERE id = @factory_id)
        THROW 50002, 'Factory does not exist', 1;
        
    INSERT INTO Products (name, factory_id, stock, last_updated)
    VALUES (@name, @factory_id, @stock, @last_updated);
END;
GO

CREATE PROCEDURE sp_UpdateProduct
    @id INT,
    @name VARCHAR(100) = NULL,
    @factory_id INT = NULL,
    @stock INT = NULL
AS
BEGIN
    IF @stock < 0
        THROW 50001, 'Stock cannot be negative', 1;
    
    IF @factory_id IS NOT NULL AND NOT EXISTS (SELECT 1 FROM Factories WHERE id = @factory_id)
        THROW 50002, 'Factory does not exist', 1;
        
    UPDATE Products
    SET name = ISNULL(@name, name),
        factory_id = ISNULL(@factory_id, factory_id),
        stock = ISNULL(@stock, stock),
        last_updated = GETDATE()
    WHERE id = @id;
END;
GO

CREATE PROCEDURE sp_DeleteProduct
    @id INT
AS
BEGIN
    IF EXISTS (SELECT 1 FROM ProductReviews WHERE product_id = @id)
        THROW 50003, 'Cannot delete product with existing reviews', 1;
        
    DELETE FROM Products WHERE id = @id;
END;
GO

CREATE PROCEDURE sp_GetProduct
    @id INT
AS
BEGIN
    SELECT p.*, f.name as factory_name
    FROM Products p
    JOIN Factories f ON p.factory_id = f.id
    WHERE p.id = @id;
END;
GO

--- tests
DECLARE @current_date DATETIME = GETDATE()

EXEC sp_InsertProduct 
    @name = 'Test Product 3',
    @factory_id = 1,
    @stock = 100,
    @last_updated = @current_date;

--pe la ~1030 il adauga
EXEC sp_GetProduct @id = 2311;

EXEC sp_UpdateProduct
    @id = 2311,
    @name = 'Updated Test Product',
    @stock = 150;

EXEC sp_GetProduct @id = 2311;

EXEC sp_DeleteProduct @id = 2311;

EXEC sp_GetProduct @id = 2311;
GO
