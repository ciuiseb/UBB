CREATE PROCEDURE sp_InsertEngineer
    @name VARCHAR(100),
    @monthly_salary DECIMAL(10,2),
    @department_id INT
AS
BEGIN
    IF @monthly_salary < 0
        THROW 50006, 'Salary cannot be negative', 1;
    
    IF NOT EXISTS (SELECT 1 FROM Departments WHERE id = @department_id)
        THROW 50007, 'Department does not exist', 1;
        
    INSERT INTO Engineers (name, monthly_salary, department_id)
    VALUES (@name, @monthly_salary, @department_id);
END;
GO

CREATE PROCEDURE sp_UpdateEngineer
    @id INT,
    @name VARCHAR(100) = NULL,
    @monthly_salary DECIMAL(10,2) = NULL,
    @department_id INT = NULL
AS
BEGIN
    IF @monthly_salary < 0
        THROW 50006, 'Salary cannot be negative', 1;
    
    IF @department_id IS NOT NULL AND NOT EXISTS (SELECT 1 FROM Departments WHERE id = @department_id)
        THROW 50007, 'Department does not exist', 1;
        
    UPDATE Engineers
    SET name = ISNULL(@name, name),
        monthly_salary = ISNULL(@monthly_salary, monthly_salary),
        department_id = ISNULL(@department_id, department_id)
    WHERE id = @id;
END;
GO

CREATE PROCEDURE sp_DeleteEngineer
    @id INT
AS
BEGIN
    DELETE FROM ProductEngineers WHERE engineer_id = @id;
    DELETE FROM Engineers WHERE id = @id;
END;
GO

CREATE PROCEDURE sp_GetEngineer
    @id INT
AS
BEGIN
    SELECT e.*, d.name as department_name
    FROM Engineers e
    JOIN Departments d ON e.department_id = d.id
    WHERE e.id = @id;
END;
GO

Select * from Engineers
--Tests
EXEC sp_InsertEngineer
    @name = 'Test Engineer',
    @monthly_salary = 5000.00,
    @department_id = 1;

EXEC sp_GetEngineer @id = 29;

EXEC sp_UpdateEngineer
    @id = 29,
    @monthly_salary = 5500.00;

EXEC sp_GetEngineer @id = 29;

EXEC sp_DeleteEngineer @id = 29;

EXEC sp_GetEngineer @id = 29;
GO
