-- view 1  -> how many products and how much totals stock there is in each factory
CREATE NONCLUSTERED INDEX IX_Factories_Id_Name
ON dbo.Factories (id, name);

CREATE NONCLUSTERED INDEX IX_Products_FactoryId_Stock
ON dbo.Products (factory_id, stock);

CREATE VIEW vw_FactoryProductsAnalytics
WITH SCHEMABINDING
AS
SELECT 
    f.id AS factory_id,
    f.name AS factory_name,
    COUNT_BIG(*) AS row_count,
    SUM(ISNULL(p.stock, 0)) AS total_stock
FROM dbo.Factories f
INNER JOIN dbo.Products p ON f.id = p.factory_id
GROUP BY f.id, f.name;
GO

CREATE Unique CLUSTERED INDEX IX_vw_FactoryProductsAnalytics 
ON vw_FactoryProductsAnalytics(factory_id);
GO

-- view 2 -> how many engineers thre are and whats the total salary per month in each department
CREATE NONCLUSTERED INDEX IX_Departments_Cover
ON dbo.Departments (id)
INCLUDE (name);

CREATE NONCLUSTERED INDEX IX_Engineers_Cover
ON dbo.Engineers (department_id)
INCLUDE (monthly_salary);

CREATE VIEW vw_DepartmentEngineersSummary
WITH SCHEMABINDING
AS
SELECT 
    d.id AS department_id,
    d.name AS department_name,
    COUNT_BIG(*) AS row_count,
    SUM(ISNULL(e.monthly_salary, 0)) AS total_salary
FROM dbo.Departments d
INNER JOIN dbo.Engineers e ON d.id = e.department_id
GROUP BY d.id, d.name;
GO

CREATE UNIQUE CLUSTERED INDEX IX_vw_DepartmentEngineersSummary
ON dbo.vw_DepartmentEngineersSummary (department_id);

-- tests
SELECT * FROM vw_FactoryProductsAnalytics;
SELECT * FROM vw_DepartmentEngineersSummary;


DROP INDEX IX_vw_FactoryProductsAnalytics ON vw_FactoryProductsAnalytics;
DROP INDEX IX_vw_DepartmentEngineersSummary ON vw_DepartmentEngineersSummary;
GO
DROP VIEW vw_FactoryProductsAnalytics;
DROP VIEW vw_DepartmentEngineersSummary;