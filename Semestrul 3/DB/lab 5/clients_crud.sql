
CREATE PROCEDURE sp_InsertClient
    @name VARCHAR(100),
    @phone_number VARCHAR(20),
    @mail VARCHAR(100)
AS
BEGIN
    IF EXISTS (SELECT 1 FROM Clients WHERE mail = @mail)
        THROW 50004, 'mail already exists', 1;
        
    INSERT INTO Clients (name, phone_number, mail)
    VALUES (@name, @phone_number, @mail);
END;
GO

CREATE PROCEDURE sp_UpdateClient
    @id INT,
    @name VARCHAR(100) = NULL,
    @phone_number VARCHAR(20) = NULL,
    @mail VARCHAR(100) = NULL
AS
BEGIN
    IF @mail IS NOT NULL AND EXISTS (SELECT 1 FROM Clients WHERE mail = @mail AND id != @id)
        THROW 50004, 'mail already exists', 1;
        
    UPDATE Clients
    SET name = ISNULL(@name, name),
        phone_number = ISNULL(@phone_number, phone_number),
        mail = ISNULL(@mail, mail)
    WHERE id = @id;
END;
GO

CREATE PROCEDURE sp_DeleteClient
    @id INT
AS
BEGIN
    IF EXISTS (SELECT 1 FROM ProductReviews WHERE client_id = @id)
        THROW 50005, 'Cannot delete client with existing reviews', 1;
        
    DELETE FROM Clients WHERE id = @id;
END;
GO

CREATE PROCEDURE sp_GetClient
    @id INT
AS
BEGIN
    SELECT * FROM Clients WHERE id = @id;
END;


select * from Clients
-- Tests
EXEC sp_InsertClient
    @name = 'John Doe',
    @phone_number = '1234567890',
    @mail = 'john.doe@test.com';

EXEC sp_GetClient @id = 31;

EXEC sp_UpdateClient
    @id = 31,
    @phone_number = '0987654321',
    @mail = 'john.updated@test.com';

EXEC sp_GetClient @id = 31;

EXEC sp_DeleteClient @id = 31;

EXEC sp_GetClient @id = 31;
GO