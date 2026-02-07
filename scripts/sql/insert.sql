INSERT INTO location (id, country) VALUES
(1, 'UK'),
(2, 'RUSSIA'),
(3, 'USA'),
(4, 'FRANCE'),
(5, 'SPAIN'),
(6, 'ITALY'),
(7, 'GERMANY');

INSERT INTO address (id, street, location_id) VALUES
(1, 'Main Street, 123', 1),      
(2, 'Lenina, 45', 2),            
(3, 'Broadway, 789', 3),         
(4, 'Champs-Élysées, 10', 4),    
(5, 'Gran Via, 67', 5),          
(6, 'Via Roma, 12', 6),          
(7, 'Brandenburg Str., 5', 7);

INSERT INTO housing (price, num_of_beds, rating, type, address_id) VALUES
(120000, 2, 4.5, 'APPARTMENT', 1),
(95000, 1, 4.2, 'ROOM', 1),
(300000, 4, 4.8, 'HOUSE', 1),
(80000, 2, 4.0, 'APPARTMENT', 2),
(60000, 1, 3.8, 'ROOM', 2),
(250000, 3, 4.6, 'HOUSE', 2),
(150000, 3, 4.7, 'APPARTMENT', 3),
(180000, 2, 4.9, 'HOTEL', 3),
(500000, 5, 4.8, 'HOUSE', 3),
(130000, 2, 4.3, 'APPARTMENT', 4),
(70000, 1, 4.1, 'HOSTEL', 4),
(220000, 3, 4.4, 'HOUSE', 4),
(90000, 2, 4.6, 'APPARTMENT', 5),
(50000, 1, 4.0, 'HOSTEL', 5),
(180000, 3, 4.7, 'HOUSE', 5),
(110000, 2, 4.5, 'APPARTMENT', 6),
(85000, 1, 4.2, 'ROOM', 6),
(280000, 4, 4.9, 'HOUSE', 6),
(100000, 2, 4.4, 'APPARTMENT', 7),
(65000, 1, 4.0, 'HOSTEL', 7),
(240000, 3, 4.6, 'HOUSE', 7);