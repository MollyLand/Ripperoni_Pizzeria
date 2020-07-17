-- Connect to ripperoni db
\c ripperoni

-- Insert values into oven
INSERT INTO Oven (IDoven, Capacity) VALUES
(1,6),
(2,6),
(3,3);

-- Insert values into Ingredient
INSERT INTO Ingredient (IDingredient, Icon, Category, IngredientName, IngredientPrice) VALUES
(1, 'flaticon-tomato', 'basic', 'tomato sauce', 0.5),
(2, 'flaticon-food', 'basic', 'mozzarella', 0.5),
(3, 'flaticon-fish', 'classy', 'tuna', 1),
(4, 'flaticon-salad', 'basic', 'spinaches', 0.5),
(5, 'flaticon-fruit', 'vegan', 'pineapple', 1),
(6, 'flaticon-meat', 'spicy', 'speck', 1.5),
(7, 'flaticon-potato', 'classy', 'roasted potatoes', 1),
(8, 'flaticon-egg', 'classy', 'egg', 1),
(9, 'flaticon-pepper', 'spicy', 'chili', 0.5),
(10, 'flaticon-seeds', 'basic', 'basil', 0.5),
(11, 'flaticon-food-1', 'vegan', 'grilled eggplant', 1.5),
(12, 'flaticon-french-fries', 'spicy', 'fries', 1),
(13, 'flaticon-fruit-2', 'vegan', 'cherry tomatoes', 1),
(14, 'flaticon-garlic', 'basic', 'garlic', 0.5),
(15, 'flaticon-milk', 'classy', 'brie', 2),
(16, 'flaticon-shrimp', 'classy', 'shrimps', 2),
(17, 'flaticon-corn', 'vegan', 'corn', 0.5),
(18, 'flaticon-beans', 'vegan', 'beans', 0.5),
(19, 'flaticon-salami', 'spicy', 'pepperoni', 1),
(20, 'flaticon-pumpkin', 'vegan', 'pumpkin cream', 1.5),
(21, 'flaticon-onion', 'basic', 'onion', 0.5),
(22, 'flaticon-bacon', 'spicy', 'bacon', 1),
(23, 'flaticon-oil', 'classy', 'black olives', 0.5),
(24, 'flaticon-pepper-1', 'spicy', 'grilled pepper', 1),
(25, 'flaticon-mushroom', 'vegan', 'mushrooms', 0.5),
(26, 'flaticon-radish', 'vegan', 'radish', 0.5),
(27, 'flaticon-oil-1', 'basic', 'olive oil', 0.5),
(28, 'flaticon-octopus', 'classy', 'seafood', 2.5),
(29, 'flaticon-mushroom-1', 'spicy', 'chiodini mushrooms', 1),
(30, 'flaticon-ham', 'basic', 'ham', 1.5),
(31, 'flaticon-vegan', 'classy', 'pistachio granola', 2);


-- Insert values into Closing invoice
INSERT INTO ClosingInvoice (IDclosing, Datetime, BillNumber, TotalBill) VALUES
(1, '2020-06-22 23:16:15-01', 4, 55),
(2, '2020-06-23 23:17:15-01', 3, 40.5),
(3, '2020-06-24 23:20:28-01', 1, 40.5);


-- Insert values into Supplier invoice
-- INSERT INTO SupplierInvoice (IDsupplierInvoice, Datetime, TotalPrice) VALUES
-- (1, '2020-04-24', 80),
-- (2, '2020-04-24', 65),
-- (3,'2020-05-01',56),
-- (4, '2020-04-24', 5);

-- Insert values into Supplier
-- INSERT INTO Supplier (VAT, CompanyName) VALUES
-- (11211111111, 'Fattoria vicentina'),
-- (37482749506, 'Ortofrutticolo Maria'),
-- (11117111112, 'Spaccio agricolo Ernesto');

-- Insert values into User
INSERT INTO Users (Username, FirstName, LastName, Password, Role, Mail, Telephone, Address) VALUES
('NicolasCage', 'Nicolas', 'Cage', '1b76c80500ece0896d7df0425cc422b947ab9c58065d9390a2d2ba94fc62f968b2e22a4997299a6fbd919c8132a966df33be066b45b518e5469eefb500658735', 'Customer', 'NicolasKimCoppola@yahoo.ue', '049000000', 'via dei Nicolas Cage'),
('SheldonCooper', 'Sheldon', 'Cooper', 'b147985e2ff99fc2d5cc90423cab14079d041ab7cc34997356e2397b057d989fa05c8f0af1afdf21d4cb7397c1b764a1a6a0e0ede48bdce537fc8d37115ea385', 'Customer', 'SheldonCooper@yahoo.ue', '049000001', 'via dei Bazinga'),
('Admin', 'Luca', 'Violi', 'd15d9a75b95d8e525ca32692e7c6807139f5f66fccdfd13259dc4cbeb9fbc6e0da017da76650720b9680f675284320368e68278f8511e2c48b59261f74fd64d7', 'Admin', 'LucaVioli@yahoo.ue', '049000002', 'via dei John Gialli'),
('LuigiVerdura', 'Luigi', 'Verdura', '7c5194abbe129cc55818f4724137ad06fc67804f0d8b24a4a0496b932724abcc2525858f5ad3ec943c235c32d4c67ecd8ff93b0956a710d81b6144b230647612', 'Delivery Guy', 'LuigiVerdura@yahoo.ue', '04900003', 'via della Verdura'),
('Spiderman', 'Peter', 'Parker', '2ea3d889ebba9f35375b4ae5949dbe3693177cc5ed1551c11a5e5c031483dc8345ceebcb4eb8353ec6de3ebd27c8bd08632c808f33ff4f684fb0ea7d06125809', 'Delivery Guy', 'peterParker@yahoo.it', '07900003', 'via dei Ragni 420'),
('MarioMortadella', 'Mario', 'Mortadella', '6cd91d77b33828f35829f0ba8d12337e82a6fde256b3e02df6fab622efaa03af5df2bdd69039c4f7df72b8bb07d537c2eb9841998fa3ad7075be472b5a626a5a', 'Cook', 'MarioMortadella@yahoo.ue', '049000004', 'via Mortadella'),
('PinoPastinaca', 'Pino', 'Pastinaca', '5ad5ddc46fb17d1f7bbd7c98c123a629757a7052f7565e73e363e899718176a027582da875bb2ca99279f0c5e6d85d388fb450443f31e2d7cda0685283b78b96', 'Cook', 'PinoPastinaca@yahoo.ue', '049000055', 'via Pastinaca'),
('ElioCarlesso', 'Elio', 'Carlesso', 'c1c23dfc9c85ca216a77bf6e6121ce533aba1613200d8ef663503ead49f250636bb5d23ce083442f6fc71bc2342ad4228ed3d6dd02ed99cbf889458de0d479cc', 'Cook', 'ElioCarlesso@yahoo.ue', '049000066', 'via Carlesso');

-- Insert values into profile
INSERT INTO Profiles (Username, Image) VALUES
('SheldonCooper', 'quokka'),
('NicolasCage', 'quokka'),
('Admin', 'quokka'),
('LuigiVerdura', 'quokka'),
('MarioMortadella', 'quokka');

--Insert values into Orders
INSERT INTO Orders (IDorder, DeliverUsername, IDclosing, CustomerUsername, RequestTime, DeliveryTime, OrderStatus, Price) VALUES
(1, 'LuigiVerdura', 1, 'NicolasCage', '2020-06-22 19:10:00-01', '2020-06-22 19:30:00-01', 'Baking', 27.5),
(2, 'LuigiVerdura', 1, 'SheldonCooper', '2020-06-22 20:15:00-01', '2020-06-22 20:45:00-01', 'InCharge', 11.5),
(3, 'Spiderman', 1, 'NicolasCage', '2020-06-22 20:24:00-01', '2020-06-22 20:45:00-01', 'InCharge', 6.5),
(4, 'Spiderman', 1, 'NicolasCage', '2020-06-22 21:17:00-01', '2020-06-22 21:30:00-01', 'InCharge', 9),
(5, 'LuigiVerdura', 2, 'SheldonCooper', '2020-06-23 19:10:00-01', '2020-06-23 19:30:00-01', 'Baking', 5.5),
(6, 'LuigiVerdura', 2, 'SheldonCooper', '2020-06-23 19:12:00-01', '2020-06-23 19:45:00-01', 'Delivering', 20),
(7, 'LuigiVerdura', 2, 'NicolasCage', '2020-06-23 19:20:00-01', '2020-06-23 20:00:00-01', 'Delivering', 15),
(8, 'Spiderman', 3, 'SheldonCooper', '2020-06-24 20:26:00-01', '2020-06-24 20:45:00-01', 'InCharge', 40.5);

--Insert Supply
-- INSERT INTO Product (IDingredient, VAT, Units, IDsupplierInvoice, UnitPrice) VALUES
-- 	(1, 11211111111, 1.5, 1, 4.8),
-- 	(2, 11211111111, 2.0, 2, 5.5),
-- 	(3, 11117111112, 0.05, 3, 6.0),
-- 	(4, 37482749506, 3,    4, 5.50);

-- Insert values into Pizza
INSERT INTO Pizza (Name, Username, Price, CreationDate, Success) VALUES
('Diavola', 'Admin', 5.5, '2020-05-08', 0),
('Rest in pizza', 'Admin', 9, '2020-05-08', 0),
('Margherita', 'Admin', 4.5, '2020-05-08', 0),
('Prosciutto e funghi', 'Admin', 6.5, '2020-05-08', 0),
('Tirolese', 'Admin', 6, '2020-05-08', 0),
('Tonno e cipolla', 'Admin', 6, '2020-05-08', 0),
('Patatosa', 'Admin', 5.5, '2020-05-15', 0),
('Hawaiana', 'Admin', 6.5, '2020-05-15', 0),
('Verdure grigliate', 'Admin', 7, '2020-05-15', 0),
('Vegetariana', 'Admin', 6.5, '2020-05-15', 0),
('Funghi misti', 'SheldonCooper', 6, '2020-05-16', 0),
('Mare', 'SheldonCooper', 9, '2020-05-18', 0),
('Dietetica', 'NicolasCage', 6, '2020-05-20', 0),
('Delizia', 'NicolasCage', 8, '2020-05-23', 0);

--Insert ConsistOf
INSERT INTO ConsistOf (PizzaName, IDingredient) VALUES
('Diavola', 1),
('Diavola', 2),
('Diavola', 19),

('Rest in pizza', 1),
('Rest in pizza', 2),
('Rest in pizza', 30),
('Rest in pizza', 25),
('Rest in pizza', 23),
('Rest in pizza', 24),
('Rest in pizza', 19),

('Margherita', 1),
('Margherita', 2),

('Prosciutto e funghi', 1),
('Prosciutto e funghi', 2),
('Prosciutto e funghi', 30),
('Prosciutto e funghi', 25),

('Tirolese', 1),
('Tirolese', 2),
('Tirolese', 6),

('Tonno e cipolla', 1),
('Tonno e cipolla', 2),
('Tonno e cipolla', 3),
('Tonno e cipolla', 21),

('Patatosa', 1),
('Patatosa', 2),
('Patatosa', 12),

('Hawaiana', 1),
('Hawaiana', 2),
('Hawaiana', 5),
('Hawaiana', 22),

('Verdure grigliate', 1),
('Verdure grigliate', 2),
('Verdure grigliate', 11),
('Verdure grigliate', 24),

('Vegetariana', 1),
('Vegetariana', 2),
('Vegetariana', 17),
('Vegetariana', 18),
('Vegetariana', 24),

('Funghi misti', 1),
('Funghi misti', 2),
('Funghi misti', 25),
('Funghi misti', 29),

('Mare', 1),
('Mare', 2),
('Mare', 16),
('Mare', 28),

('Dietetica', 1),
('Dietetica', 2),
('Dietetica', 13),
('Dietetica', 27),

('Delizia', 1),
('Delizia', 2),
('Delizia', 20),
('Delizia', 31);

--insert Coupon
INSERT INTO Coupon (IDcoupon, Username, Percentage, IDorder) VALUES
(123456789, 'NicolasCage', 25, NULL),
(234567891, 'SheldonCooper', 15, NULL);

--insert Cook
INSERT INTO Cook (Username, IDoven, IDorder) VALUES
('MarioMortadella', 1, 1),
('PinoPastinaca', 2, 2),
('ElioCarlesso', 3, 3),
('MarioMortadella', 1, 4),
('MarioMortadella', 1, 5),
('PinoPastinaca', 2, 6),
('ElioCarlesso', 3, 7),
('MarioMortadella', 1, 8);

--insert Contain
INSERT INTO Contain (IDorder, PizzaName, Quantity) VALUES
(1, 'Vegetariana', 2),
(1, 'Patatosa', 1),
(1, 'Margherita', 2),
(2, 'Diavola', 1),
(2, 'Tirolese', 1),
(3, 'Hawaiana', 1),
(4, 'Margherita', 2),
(5, 'Patatosa', 1),
(6, 'Margherita', 2),
(6, 'Diavola', 2),
(7, 'Margherita', 2),
(7, 'Tirolese', 1),
(8, 'Mare', 1),
(8, 'Diavola', 2),
(8, 'Margherita', 1),
(8, 'Delizia', 2);

--insert SalaryRegister
-- INSERT INTO SalaryRegister (IDsalary, Datetime, Username, Amount, Iban, FiscalCode) VALUES
--    	(1, '2020-4-27', 'LuigiVerdura', 1200, 'IT10aaaa1010101758695834567', 'NRPMHR47E09L940X'),
--    	(2, '2020-4-27', 'MarioMortadella', 1100, 'IT10caaa1010101758695834568', 'NPEMPL97E29L889A');
