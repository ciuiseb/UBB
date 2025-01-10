USE LAB4
GO

INSERT INTO Tests(Name)
VALUES ('insert_table'),
       ('delete_table'),
       ('select_views')
GO

SELECT * FROM Tests;

INSERT INTO TestViews(TestID, ViewID)
VALUES (3, 1),(3, 2),(3, 3)
GO

SELECT * FROM TestViews;

INSERT INTO TestTables VALUES
(1, 1, 1000, 1), -- Departments
(1, 2, 1000, 2), -- Products
(1, 3, 1000, 3), -- ProductsComposition
(2, 1, 1000, 3),
(2, 2, 1000, 2),
(2, 3, 1000, 1)
GO

--  insert procedures
CREATE OR ALTER PROCEDURE InsertDepartments(@rows int)
AS
BEGIN
    DECLARE @id int
    DECLARE @name VARCHAR(50)
    DECLARE @i int
    DECLARE @lastId int
    SET @id = 1100
    SET @i = 1

    WHILE @i <= @rows
    BEGIN
        SET @id = 1100 + @i
        SELECT TOP 1 @lastId = id FROM Departments ORDER BY id DESC
        IF @lastId > 1100
            SET @id = @lastId + 1
        SET @name = 'Department_' + CONVERT(VARCHAR(5), @id)
        INSERT INTO Departments(name, floor, room_number) VALUES(@name, 1, 101)
        SET @i = @i + 1
    END
END
GO

CREATE OR ALTER PROCEDURE InsertProducts(@rows int)
AS
BEGIN
    IF NOT EXISTS (SELECT 1 FROM Factories)
        INSERT INTO Factories(name, address, number_of_employees) VALUES('Factory1', 'Address1', 100);
        
    DECLARE @factory_id int = (SELECT TOP 1 id FROM Factories);
    DECLARE @id int
    DECLARE @name varchar(50)
    DECLARE @i int
    DECLARE @lastId int
    SET @id = 1000
    SET @i = 1

    WHILE @i <= @rows
    BEGIN
        SET @id = 1000 + @i
        SELECT TOP 1 @lastId = id FROM Products ORDER BY id DESC
        IF @lastId > 1000
            SET @id = @lastId + 1
        SET @name = 'Product_' + CONVERT(varchar(5), @id)
        INSERT INTO Products(name, factory_id, stock, last_updated) VALUES (@name, @factory_id, 100, GETDATE())
        SET @i = @i + 1
    END
END
GO

CREATE OR ALTER PROCEDURE InsertProductsComposition(@rows int)
AS
BEGIN
    IF NOT EXISTS (SELECT 1 FROM Materials)
        INSERT INTO Materials(name) VALUES('Material1');
        
    DECLARE @material_id int = (SELECT TOP 1 id FROM Materials);
    DECLARE @i int
    SET @i = @rows

    exec InsertProducts @rows

    DECLARE @idP int, @name varchar(50)
    DECLARE cursorProducts CURSOR SCROLL FOR
    SELECT id, name FROM Products
    OPEN cursorProducts;
    FETCH LAST FROM cursorProducts INTO @idP, @name;

    WHILE @i > 0 AND @@FETCH_STATUS = 0
    BEGIN
        INSERT INTO ProductsComposition(product_id, material_id) VALUES (@idP, @material_id)
        FETCH PRIOR FROM cursorProducts INTO @idP, @name
        SET @i = @i - 1
    END

    CLOSE cursorProducts
    DEALLOCATE cursorProducts
END
GO

-- Delete procedures
CREATE OR ALTER PROCEDURE DeleteDepartments(@rows int)
AS
BEGIN
    DECLARE @id int
    DECLARE @i int
    DECLARE @lastId int
    SET @id = 1100
    SET @i = @rows

    WHILE @i > 0
    BEGIN
        SET @id = 1100 + @i
        SELECT TOP 1 @lastId = id FROM Departments ORDER BY id DESC
        IF @lastId > @id
            SET @id = @lastId
        DELETE FROM Departments WHERE id = @id
        SET @i = @i - 1
    END
END
GO

CREATE OR ALTER PROCEDURE DeleteProducts(@rows int)
AS
BEGIN
    DECLARE @id int
    DECLARE @i int
    DECLARE @lastId int
    SET @id = 1000
    SET @i = @rows

    WHILE @i > 0
    BEGIN
        SET @id = 1000 + @i
        SELECT TOP 1 @lastId = id FROM Products ORDER BY id DESC
        IF @lastId > @id
            SET @id = @lastId
        DELETE FROM Products WHERE id = @id
        SET @i = @i - 1
    END
END
GO

CREATE OR ALTER PROCEDURE DeleteProductsComposition(@rows int)
AS
BEGIN
    DECLARE @id int
    DECLARE @i int
    DECLARE @lastId int
    SET @id = 1000
    SET @i = @rows

    WHILE @i > 0
    BEGIN
        SET @id = 1000 + @i
        SELECT TOP 1 @lastId = Products.id FROM Products ORDER BY Products.id DESC
        IF @lastId > @id
            SET @id = @lastId
        DELETE FROM ProductsComposition WHERE product_id = @id
        SET @i = @i - 1
    END
END
GO

-- Generic procedures
CREATE OR ALTER PROCEDURE insert_table(@Table varchar(50))
AS
BEGIN
    IF @Table = 'Departments'
        EXEC InsertDepartments 1000
    IF @Table = 'Products'
        EXEC InsertProducts 1000
    IF @Table = 'ProductsComposition'
        EXEC InsertProductsComposition 1000
END
GO

CREATE OR ALTER PROCEDURE delete_table(@Table varchar(50))
AS
BEGIN
    IF @Table = 'Departments'
        EXEC DeleteDepartments 1000
    IF @Table = 'Products'
        EXEC DeleteProducts 1000
    IF @Table = 'ProductsComposition'
        EXEC DeleteProductsComposition 1000
END
GO

CREATE OR ALTER PROCEDURE select_view(@View VARCHAR(20))
AS
BEGIN
    IF @View = 'view_1'
        SELECT * FROM view_1
    IF @View = 'view_2'
        SELECT * FROM view_2
    IF @View = 'view_3'
        SELECT * FROM view_3
END
GO

CREATE OR ALTER PROCEDURE main_test (@Table VARCHAR(20))
AS
BEGIN
    DECLARE @t1 datetime, @t2 datetime, @t3 datetime
    DECLARE @desc VARCHAR(2000)
    DECLARE @testID int
    DECLARE @id_table int
    DECLARE @id_view int
    DECLARE @VTable varchar(50) = 'view_' + 
        CAST((SELECT TableID FROM Tables WHERE Name = @Table) AS VARCHAR)

    SELECT @id_table = TableID FROM Tables WHERE Name = @Table
    SELECT @id_view = ViewID FROM Views WHERE Name = @VTable

    SET @t1 = GETDATE()
    EXEC delete_table @Table
    EXEC insert_table @Table
    SET @t2 = GETDATE()
    EXEC select_view @VTable
    SET @t3 = GETDATE()

    SET @desc = 'delete - insert - ' + @Table
    INSERT INTO TestRuns VALUES (@desc, @t1, @t2)
    SELECT TOP 1 @testID = TestRunID FROM TestRuns ORDER BY TestRunID DESC
    INSERT INTO TestRunTables VALUES (@testID, @id_table, @t1, @t2)
    INSERT INTO TestRunViews VALUES (@testID, @id_view, @t2, @t3)
END
GO

-- Run tests
EXEC main_test 'Departments'
EXEC main_test 'Products'
EXEC main_test 'ProductsComposition'

-- View results
SELECT * FROM TestRuns
SELECT * FROM TestRunTables
SELECT * FROM TestRunViews

Delete From TestRuns