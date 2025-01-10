CREATE PROCEDURE sp_InsertDepartment
    @name VARCHAR(100),
    @floor INT,
    @room_number VARCHAR(10)
AS
BEGIN
    IF @floor < 1
        THROW 50008, 'Floor number must be positive', 1;
        
    INSERT INTO Departments (name, floor, room_number)
    VALUES (@name, @floor, @room_number);
END;
GO

CREATE PROCEDURE sp_UpdateDepartment
    @id INT,
    @name VARCHAR(100) = NULL,
    @floor INT = NULL,
    @room_number VARCHAR(10) = NULL
AS
BEGIN
    IF @floor < 1
        THROW 50008, 'Floor number must be positive', 1;
        
    UPDATE Departments
    SET name = ISNULL(@name, name),
        floor = ISNULL(@floor, floor),
        room_number = ISNULL(@room_number, room_number)
    WHERE id = @id;
END;
GO

CREATE PROCEDURE sp_DeleteDepartment
    @id INT
AS
BEGIN
    IF EXISTS (SELECT 1 FROM Engineers WHERE department_id = @id)
        THROW 50009, 'Cannot delete department with assigned engineers', 1;
        
    DELETE FROM Departments WHERE id = @id;
END;
GO

CREATE PROCEDURE sp_GetDepartment
    @id INT
AS
BEGIN
    SELECT * FROM Departments WHERE id = @id;
END;
GO

Select * from Departments
--Tests
EXEC sp_InsertDepartment
    @name = 'Test Department',
    @floor = 3,
    @room_number = '301';

-- 2310
EXEC sp_GetDepartment @id = 2310;

-- Update Department
EXEC sp_UpdateDepartment
    @id = 2310,
    @name = 'Updated Department',
    @floor = 4;

-- Get Updated Department
EXEC sp_GetDepartment @id = 2310;

-- Delete Department
EXEC sp_DeleteDepartment @id = 2310;

-- Verify Delete (should return no rows)
EXEC sp_GetDepartment @id = 2310;
GO