USE [LAB1];

--adding some factories
INSERT INTO Factories (name) VALUES
('Fabrica din Sud'),
('Fabrica din Nord'),
('Fabrica din Est'),
('Fabrica din Vest');

-- adding some products
INSERT INTO Products (name, factory_id) VALUES 
('Resistor', 1),          
('Capacitor', 1),        
('Transistor', 2),        
('Microcontroller', 2),   
('LED', 3);

-- adding some materials
INSERT INTO Materials (name) VALUES 
('Copper'),
('Silicon'),
('Plastic'),
('Aluminum'),
('Gold'),
('Ceramic');
INSERT INTO Materials (name) VALUES 
('Tin'), 
('Lead'), 
('Tantalum'), 
('Gallium Arsenide'), 
('Polyester'), 
('Glass'), 
('Epoxy');

-- adding some clients 
INSERT INTO Clients (name) VALUES 
('Magazinul cel mai mare'),
('Magazinul putin mai mic'),
('Cel mai mic magazin'),
('Corporatie multimiliardara'),
('Corporatie la inceput'),
('Retailer Alin'),
('Electronics Hub'),
('Tech Store'),
('Magazin de paine');


-- adding some connections
INSERT INTO ClientsFactories (client_id, factory_id) VALUES 
(1, 1),
(1, 2),
(2, 3),
(3, 4),
(4, 1),
(5, 2),
(6, 3),
(7, 4),
(8, 1),
(9, 2),
(1, 3);

-- adding some engenieers
INSERT INTO Engineers (name, monthly_salary) VALUES 
('Andrei Popescu', 6000),
('Ioana Ionescu', 7500),
('Mihai Georgescu', 7000),
('Elena Vasilescu', 8000),
('Sorin Dumitrescu', 6500),
('Raluca Stan', 7200),
('Gabriel Radu', 6800);

-- connecting products to their fathers
INSERT INTO ProductEngineers (product_id, engineer_id) VALUES 
(1, 1),
(1, 2),
(1, 3),
(2, 2),
(2, 4),
(2, 5),
(3, 3),
(3, 5),
(3, 6),
(4, 1),
(4, 4),
(4, 7),
(5, 2),
(5, 3),
(5, 6),
(7, 2),
(7, 4),
(7, 6);

-- adding some completely true and unbiased reviews
INSERT INTO ProductReviews (product_id, client_id, rating, comment) VALUES 
(1, 1, 5, 'Excellent product!'),
(2, 2, 4, 'Very good quality.'),
(3, 3, 3, 'Satisfactory performance.'),
(4, 1, 4, 'Good value for money.'),
(5, 2, 2, 'Not as expected.'),
(7, 1, 4, 'Reliable and durable.');

--moving the stock data
UPDATE Products
SET stock = CASE 
    WHEN id = 1 THEN 150
    WHEN id = 2 THEN 200
    WHEN id = 3 THEN 300
    WHEN id = 4 THEN 250
    WHEN id = 5 THEN 180
    WHEN id = 7 THEN 220
    ELSE stock
END, last_updated = getdate();

-- adding some departments
INSERT INTO Departments (name, floor, room_number)VALUES 
('Research and Development', 2, 305),
('Quality Assurance', 3, 412),
('Manufacturing', 1, 101),
('Procurement', 4, 501),
('Logistics and Shipping', 2, 215);

-- updating the factories table
UPDATE Factories
SET adress = CASE 
    WHEN id = 1 THEN 'Strada Sudului 15, Bucuresti'
    WHEN id = 2 THEN 'Bulevardul Nordului 25, Cluj-Napoca'
    WHEN id = 3 THEN 'Calea Estului 12, Iasi'
    WHEN id = 4 THEN 'Aleea Vestului 7, Timisoara'
    ELSE adress
END,
number_of_employees = CASE 
    WHEN id = 1 THEN 150
    WHEN id = 2 THEN 120
    WHEN id = 3 THEN 200
    WHEN id = 4 THEN 180
    ELSE number_of_employees
END;

--updating the engineers table
UPDATE Engineers
SET department_id = CASE 
    WHEN id = 1 THEN 1
    WHEN id = 2 THEN 2
    WHEN id = 3 THEN 3
    WHEN id = 4 THEN 4
    WHEN id = 5 THEN 5
    WHEN id = 6 THEN 1
    WHEN id = 7 THEN 3
    ELSE department_id
END;

UPDATE Clients
SET phone_number = CASE id
    WHEN 1 THEN '0721234567' 
    WHEN 2 THEN '0732345678' 
    WHEN 3 THEN '0743456789' 
    WHEN 4 THEN '0754567890' 
    WHEN 5 THEN '0765678901' 
    WHEN 6 THEN '0776789012' 
    WHEN 7 THEN '0787890123' 
    WHEN 8 THEN '0798901234' 
    WHEN 9 THEN '0709012345' 
END,     mail = CASE id
        WHEN 1 THEN 'contact@magazinulcel.ro'
        WHEN 2 THEN 'office@magazinulpublic.ro'
        WHEN 3 THEN 'contact@celmaimimagazin.ro'
        WHEN 4 THEN 'office@corporatiemul.ro'
        WHEN 5 THEN 'contact@corporatie.ro'
        WHEN 6 THEN 'office@retaileralim.ro'
        WHEN 7 THEN 'contact@electronicshub.ro'
        WHEN 8 THEN 'info@techstore.ro'
        WHEN 9 THEN 'contact@magazinpai.ro'
    END
WHERE id BETWEEN 1 AND 9;

-- Additional Factories (4 existing + 20 new = 24)
INSERT INTO Factories (name, adress, number_of_employees) VALUES
('Fabrica Smart Tech', 'Strada Industriei 10, Brasov', 145),
('Fabrica MicroElectro', 'Bulevardul Unirii 20, Constanta', 165),
('Fabrica CircuitPro', 'Aleea Tehnicii 5, Craiova', 175),
('Fabrica ElectroComp', 'Strada Progresului 30, Galati', 130),
('Fabrica TechnoCore', 'Bulevardul Muncii 15, Oradea', 190),
('Fabrica ElectroHub', 'Calea Victoriei 25, Ploiesti', 140),
('Fabrica InnovaTech', 'Strada Pacii 8, Arad', 155),
('Fabrica CircuitMaster', 'Aleea Centrala 12, Pitesti', 185),
('Fabrica ProSystem', 'Bulevardul Principal 22, Sibiu', 170),
('Fabrica ElectroSys', 'Strada Principala 17, Bacau', 160),
('Fabrica TechPro', 'Calea Nationala 9, Baia Mare', 145),
('Fabrica SmartSys', 'Aleea Industriala 14, Buzau', 175),
('Fabrica ElectroNet', 'Bulevardul Central 28, Botosani', 150),
('Fabrica CircuitNet', 'Strada Tehnicii 11, Satu Mare', 165),
('Fabrica TechMaster', 'Calea Revolutiei 19, Suceava', 180),
('Fabrica ProTech', 'Aleea Victoriei 7, Targu Mures', 170),
('Fabrica SmartCore', 'Bulevardul Pacii 16, Deva', 155),
('Fabrica ElectroMaster', 'Strada Centrala 23, Piatra Neamt', 145),
('Fabrica CircuitHub', 'Calea Unirii 13, Targoviste', 160),
('Fabrica TechSystem', 'Aleea Progresului 21, Focsani', 175);

-- Additional Products (5 existing + 20 new = 25)
INSERT INTO Products (name, factory_id, stock) VALUES 
('Diode', 3, 280),
('Inductor', 4, 190),
('Logic Gate', 1, 220),
('OpAmp', 2, 170),
('Crystal Oscillator', 3, 150),
('MOSFET', 4, 240),
('Power Supply', 1, 130),
('Heat Sink', 2, 200),
('Circuit Board', 3, 160),
('Display Module', 4, 180),
('Memory Chip', 1, 210),
('Voltage Regulator', 2, 230),
('Relay', 3, 170),
('Switch', 4, 190),
('Battery Holder', 1, 220),
('Sensor Module', 2, 160),
('Fuse', 3, 240),
('Connector', 4, 200),
('Motor Driver', 1, 150),
('Solar Cell', 2, 180);

-- Additional Materials (13 existing + 20 new = 33)
INSERT INTO Materials (name) VALUES 
('Carbon'),
('Silver'),
('Nickel'),
('Zinc'),
('Steel'),
('Brass'),
('Bronze'),
('Iron'),
('Platinum'),
('Palladium'),
('Rubber'),
('Fiberglass'),
('PVC'),
('Polyethylene'),
('Polypropylene'),
('Nylon'),
('Mica'),
('Ferrite'),
('Germanium'),
('Graphene');

-- Additional Clients (9 existing + 20 new = 29)
INSERT INTO Clients (name, phone_number, mail) VALUES 
('Digital Solutions', '0710123456', 'contact@digitalsolutions.ro'),
('ElectroMart', '0711234567', 'office@electromart.ro'),
('TechZone', '0712345678', 'info@techzone.ro'),
('Circuit World', '0713456789', 'office@circuitworld.ro'),
('ElectroMax', '0714567890', 'contact@electromax.ro'),
('ComponentPro', '0715678901', 'sales@componentpro.ro'),
('SmartTech Store', '0716789012', 'info@smarttech.ro'),
('ElectroDepot', '0717890123', 'office@electrodepot.ro'),
('TechSupply', '0718901234', 'contact@techsupply.ro'),
('CircuitShop', '0719012345', 'sales@circuitshop.ro'),
('ElectroHub Plus', '0720123456', 'info@electrohubplus.ro'),
('TechMart Pro', '0721234567', 'office@techmartpro.ro'),
('Digital Components', '0722345678', 'contact@digitalcomp.ro'),
('ElectroWorld', '0723456789', 'sales@electroworld.ro'),
('Circuit Central', '0724567890', 'info@circuitcentral.ro'),
('TechParts', '0725678901', 'office@techparts.ro'),
('SmartComponents', '0726789012', 'contact@smartcomp.ro'),
('ElectroPro Store', '0727890123', 'sales@electropro.ro'),
('Digital Hub', '0728901234', 'info@digitalhub.ro'),
('TechWarehouse', '0729012345', 'office@techwarehouse.ro');

-- Additional Engineers (7 existing + 20 new = 27)
INSERT INTO Engineers (name, monthly_salary, department_id) VALUES 
('Adrian Munteanu', 7100, 2),
('Maria Popa', 6900, 3),
('Cristian Stoica', 7300, 1),
('Diana Diaconu', 6700, 4),
('Victor Marinescu', 7400, 5),
('Andreea Nistor', 6600, 2),
('Tudor Barbu', 7200, 3),
('Laura Florescu', 6800, 1),
('Bogdan Marin', 7500, 4),
('Carmen Stefan', 6900, 5),
('Rares Moldovan', 7100, 2),
('Denisa Preda', 6700, 3),
('Vlad Constantin', 7300, 1),
('Alexandra Rusu', 6800, 4),
('Daniel Serban', 7200, 5),
('Monica Dragomir', 6600, 2),
('Octavian Nagy', 7400, 3),
('Simona Lungu', 6900, 1),
('Razvan Dobre', 7000, 4),
('Alina Toma', 7300, 5);

-- Additional Departments (5 existing + 20 new = 25)
INSERT INTO Departments (name, floor, room_number) VALUES 
('Product Design', 3, 301),
('Testing Lab', 2, 202),
('Assembly', 1, 105),
('Electronics Lab', 4, 401),
('Component Design', 2, 208),
('Reliability Testing', 3, 315),
('Prototype Development', 1, 110),
('Circuit Design', 4, 405),
('Materials Testing', 2, 220),
('Hardware Development', 3, 320),
('Software Integration', 1, 115),
('Quality Control', 4, 410),
('Systems Integration', 2, 225),
('Technical Support', 3, 325),
('Production Planning', 1, 120),
('Equipment Maintenance', 4, 415),
('Safety Compliance', 2, 230),
('Process Engineering', 3, 330),
('Environmental Testing', 1, 125),
('Project Management', 4, 420);

-- Additional ProductReviews (6 existing + 20 new = 26)
INSERT INTO ProductReviews (product_id, client_id, rating, comment) VALUES 
(1, 3, 5, 'Perfect for our needs'),
(2, 4, 4, 'High quality product'),
(3, 5, 5, 'Exceptional performance'),
(4, 6, 3, 'Meets basic requirements'),
(5, 7, 4, 'Good build quality'),
(1, 8, 5, 'Highly recommended'),
(2, 9, 4, 'Reliable product'),
(3, 1, 5, 'Outstanding quality'),
(4, 2, 3, 'Decent performance'),
(5, 3, 4, 'Well-made product'),
(1, 4, 5, 'Excellent service life'),
(2, 5, 4, 'Good value proposition'),
(3, 6, 5, 'Superior quality'),
(4, 7, 4, 'Reliable performance'),
(5, 8, 3, 'Acceptable quality'),
(1, 9, 5, 'Top-notch product'),
(2, 1, 4, 'Very satisfied'),
(3, 2, 5, 'Exceeds expectations'),
(4, 3, 4, 'Solid performance'),
(5, 4, 5, 'Great product overall');

-- Additional ClientsFactories connections
INSERT INTO ClientsFactories (client_id, factory_id) VALUES 
(10, 5),  -- Connecting new clients with new factories
(11, 6),
(12, 7),
(13, 8),
(14, 9),
(15, 10),
(16, 11),
(17, 12),
(18, 13),
(19, 14),
(20, 15),
(21, 16),
(22, 17),
(23, 18),
(24, 19),
(25, 20),
(26, 21),
(27, 22),
(28, 23),
(29, 24);

-- Additional ProductEngineers (connecting new products with engineers)
INSERT INTO ProductEngineers (engineer_id, product_id) VALUES 
(6, 8),   -- MOSFET
(6, 9),
(6, 10),
(7, 11),  -- Power Supply
(7, 12),
(7, 13),
(8, 14),  -- Heat Sink
(8, 15),
(8, 16),
(9, 17),  -- Circuit Board
(9, 18),
(9, 19),
(10, 20), -- Display Module
(10, 21),
(10, 22),
(11, 23), -- Memory Chip
(11, 24),
(11, 25),
(12, 26), -- Voltage Regulator
(12, 27); -- Assigning newer engineers to newer products