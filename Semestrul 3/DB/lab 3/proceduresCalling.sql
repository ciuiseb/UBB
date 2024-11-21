USE [LAB3]
CREATE TABLE DatabaseVersions (
    version_id INT PRIMARY KEY,
    description VARCHAR(200)
);
GO

CREATE FUNCTION GetMaxVersion()
RETURNS INT
AS
BEGIN
    RETURN 5;
END;
GO

-- Version implementation selector
CREATE PROCEDURE proc_ImplementVersion
    @version INT
AS
BEGIN
    IF @version = 1 EXEC proc_ChangeEngineerSalaryToDecimal;
    ELSE IF @version = 2 EXEC proc_AddDefaultStockToProducts;
    ELSE IF @version = 3 EXEC proc_CreateNewDepartmentTable;
    ELSE IF @version = 4 EXEC proc_AddManagerToFactories;
    ELSE IF @version = 5 EXEC proc_AddMaterialsFKToProducts;
END;
GO

-- Upgrade procedure
CREATE PROCEDURE proc_UpgradeToVersion
    @targetVersion INT
AS
BEGIN
	IF @targetVersion < 0 OR @targetVersion > dbo.GetMaxVersion()
		BEGIN
			RAISERROR('Invalid version number', 16,1);
			RETURN;
		END

    DECLARE @currentVersion INT;
    SELECT @currentVersion = ISNULL(MAX(version_id), 0) FROM DatabaseVersions;
    
    WHILE @currentVersion < @targetVersion
    BEGIN
        SET @currentVersion = @currentVersion + 1;
        EXEC proc_ImplementVersion @currentVersion;
    END
END;
GO

-- Version undo selector
CREATE PROCEDURE proc_UndoVersion
    @version INT
AS
BEGIN
		IF @version = 1 EXEC proc_RemoveMaterialsFKFromProducts;
		ELSE IF @version = 2 EXEC proc_RemoveManagerFromFactories;
		ELSE IF @version = 3 EXEC proc_DropNewDepartmentTable;
		ELSE IF @version = 4 EXEC proc_RemoveDefaultStockFromProducts;
		ELSE IF @version = 5 EXEC proc_UndoEngineerSalaryToInt;
	END;
GO

-- Downgrade procedure
CREATE PROCEDURE proc_DowngradeToVersion
    @targetVersion INT
AS
BEGIN
	IF @targetVersion < 0 OR @targetVersion > dbo.GetMaxVersion()
		BEGIN
			RAISERROR('Invalid version number.', 16,1);
			RETURN;
		END
    DECLARE @currentVersion INT;
    SELECT @currentVersion = MAX(version_id) FROM DatabaseVersions;
    
    WHILE @currentVersion > @targetVersion
    BEGIN
        EXEC proc_UndoVersion @currentVersion;
        SET @currentVersion = @currentVersion - 1;
    END
END;
GO

--EXEC proc_UpgradeToVersion 5;

--EXEC proc_DowngradeToVersion 0;

--SELECT ISNULL(MAX(version_id),0) FROM DatabaseVersions;

--SELECT * FROM DatabaseVersions;
