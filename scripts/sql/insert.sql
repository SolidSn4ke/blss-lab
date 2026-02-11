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

INSERT INTO user_table (username, name, family_name, password, access_token) VALUES
('Mike', 'Mike', 'Jonson', '03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4', '550e8400-e29b-41d4-a716-446655440000'),
('tester', 'tester', 'tester', '03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4', '123e4567-e89b-12d3-a456-426614174000'),
('user', 'Vlad', 'Silintsev', '03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4', '6ba7b810-9dad-11d1-80b4-00c04fd430c8');

INSERT INTO housing (id, price, num_of_beds, rating, type, address_id, belongs_to) VALUES
(1, 12000, 2, 4.5, 'APPARTMENT', 1, 'Mike'),
(2, 9500, 1, 4.2, 'ROOM', 1, 'Mike'),
(3, 3000, 4, 4.8, 'HOUSE', 1, 'Mike'),
(4, 8000, 2, 4.0, 'APPARTMENT', 2, 'tester'),
(5, 6000, 1, 3.8, 'ROOM', 2, 'Mike'),
(6, 25000, 3, 4.6, 'HOUSE', 2, 'tester'),
(7, 15000, 3, 4.7, 'APPARTMENT', 3, 'Mike'),
(8, 18000, 2, 4.9, 'HOTEL', 3, 'tester'),
(9, 5000, 5, 4.8, 'HOUSE', 3, 'Mike'),
(10, 13000, 2, 4.3, 'APPARTMENT', 4, 'Mike'),
(11, 7000, 1, 4.1, 'HOSTEL', 4, 'Mike'),
(12, 22000, 3, 4.4, 'HOUSE', 4, 'Mike'),
(13, 9000, 2, 4.6, 'APPARTMENT', 5, 'tester'),
(14, 5000, 1, 4.0, 'HOSTEL', 5, 'tester'),
(15, 18000, 3, 4.7, 'HOUSE', 5, 'tester'),
(16, 11000, 2, 4.5, 'APPARTMENT', 6, 'tester'),
(17, 8500, 1, 4.2, 'ROOM', 6, 'tester'),
(18, 28000, 4, 4.9, 'HOUSE', 6, 'tester'),
(19, 10000, 2, 4.4, 'APPARTMENT', 7, 'tester'),
(20, 6500, 1, 4.0, 'HOSTEL', 7, 'tester'),
(21, 24000, 3, 4.6, 'HOUSE', 7, 'tester');

INSERT INTO booking (id, check_in, check_out, booked_by, housing_id, created_at, status, total_price, adults_count, child_count, infants_count, pet_count) VALUES
(1, '2026-03-01', '2026-03-04', 'user', 1, '2026-01-10 14:30:45.123456', 'PENDING', 24000, 2, 0, 0, 0),
(2, '2026-04-15', '2026-04-19', 'user', 4, '2026-02-01 09:15:30.500000', 'PENDING', 16000, 1, 1, 0, 0);