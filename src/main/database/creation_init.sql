-- Database Creation
CREATE DATABASE ripperoni OWNER POSTGRES ENCODING = 'UTF8';
-- Connect to ripperoni db to create data for its 'public' schema
\c ripperoni
-- Create a new data type
CREATE TYPE role AS ENUM (
 'Customer',
  	'Delivery Guy',
  	'Cook',
  	'Admin'
);
CREATE TYPE orderstatus AS ENUM (
	'InCharge',
	'Baking',
	'Delivering',
	'Delivered'
);

CREATE TYPE category AS ENUM (
    'basic',
    'vegan',
    'spicy',
    'classy'
);

-- Create new domains:

--Not case sensitive
CREATE DOMAIN fiscalcode AS CHAR(16)
	CONSTRAINT properfiscalcode CHECK (((VALUE)::text~*'[A-Za-z]{6}[0-9]{2}[abcdehlmprstABCDEHLMPRST]{1}[0-9]{2}[A-Za-z]{1}[0-9]{3}[A-Za-z]{1}$|([0-9]{11})'::text));


CREATE DOMAIN VATdomain AS CHAR(11)
	CONSTRAINT propervat CHECK (((VALUE)::text ~* '[0-9]{11}'::text));


CREATE DOMAIN IBAN AS  CHAR(27)
	CONSTRAINT properiban CHECK (((VALUE)::text~* '[a-zA-Z]{2}[0-9]{2}[a-zA-Z0-9]{4}[0-9]{7}([a-zA-Z0-9]?){0,16}'::text));

-- Table Creation
-- CAVEAT: remember to create table in the right order wrt foreign keys

-- Oven
CREATE TABLE Oven(
IDoven SERIAL,
Capacity INT NOT NULL CHECK (Capacity>0),
PRIMARY KEY (IDoven)
);

-- Ingredient
CREATE TABLE Ingredient(
	IDingredient SERIAL,
    Category category NOT NULL,
    Icon VARCHAR(32) NOT NULL,
	IngredientName VARCHAR NOT NULL,
	IngredientPrice NUMERIC NOT NULL,
	PRIMARY KEY (IDingredient)
);
-- Closing Invoice
CREATE TABLE ClosingInvoice (
	IDclosing SERIAL,
	Datetime TIMESTAMPTZ NOT NULL,
	BillNumber INT NOT NULL CHECK (BillNumber>=0),
	TotalBill NUMERIC NOT NULL CHECK (TotalBill>=0),
	PRIMARY KEY (IDclosing)
);
-- Supplier Invoice
CREATE TABLE SupplierInvoice(
	IDsupplierInvoice SERIAL,
	Datetime DATE NOT NULL,
	TotalPrice NUMERIC NOT NULL CHECK (TotalPrice>=0),
	PRIMARY KEY (IDsupplierInvoice)
);
-- Supplier
CREATE TABLE Supplier(
	VAT VATdomain,
	CompanyName VARCHAR NOT NULL,
	PRIMARY KEY (VAT)
);
-- User
CREATE TABLE Users(
	Username VARCHAR,
	FirstName VARCHAR NOT NULL,
	LastName VARCHAR NOT NULL,
	Password VARCHAR NOT NULL,
	Role role NOT NULL,
	Mail VARCHAR NOT NULL UNIQUE,
	Telephone VARCHAR NOT NULL,
	Address VARCHAR NOT NULL,
	PRIMARY KEY(Username)
);
-- Profile
CREATE TABLE Profiles(
	Username VARCHAR,
	Image BYTEA,
	PRIMARY KEY (Username),
  FOREIGN KEY (Username) REFERENCES Users(Username)
);
-- Order
CREATE TABLE Orders(
	IDorder SERIAL,
	DeliverUsername VARCHAR,
	IDclosing SERIAL,
	CustomerUsername VARCHAR NOT NULL,
	RequestTime TIMESTAMPTZ NOT NULL,
	DeliveryTime TIMESTAMPTZ,
	OrderStatus  orderstatus NOT NULL,
	Price NUMERIC CHECK (Price>=0),
	PRIMARY KEY (IDorder),
	FOREIGN KEY (DeliverUsername) REFERENCES Users (Username),
	FOREIGN KEY (CustomerUsername) REFERENCES Users (Username),
	FOREIGN KEY (IDclosing) REFERENCES ClosingInvoice (IDclosing),
	CHECK (DeliveryTime > RequestTime)
);
-- Coupon
CREATE TABLE Coupon (
	IDcoupon SERIAL,
	Username VARCHAR NOT NULL,
	Percentage INTEGER NOT NULL CHECK(Percentage > 0),
	IDorder INT,
	PRIMARY KEY (IDcoupon),
	FOREIGN KEY (Username) REFERENCES Users(Username),
  FOREIGN KEY (IDorder) REFERENCES Orders(IDorder)
);
-- Supply → PRODUCT
CREATE TABLE Product(
	IDingredient SERIAL,
	VAT VATdomain NOT NULL,
	Units FLOAT NOT NULL CHECK (Units > 0),
	IDsupplierInvoice SERIAL,
	UnitPrice NUMERIC NOT NULL CHECK (Unitprice >= 0),
	PRIMARY KEY(VAT, IDingredient, IDsupplierInvoice),
	FOREIGN KEY(IDingredient) REFERENCES Ingredient(IDingredient),
	FOREIGN KEY(VAT) REFERENCES Supplier(VAT),
	FOREIGN KEY(IDsupplierInvoice) REFERENCES SupplierInvoice(IDsupplierInvoice)
);
-- Pizza
CREATE TABLE Pizza (
	Name VARCHAR,
	Username VARCHAR NOT NULL,
	Price NUMERIC NOT NULL CHECK ( Price > 0),
	CreationDate DATE NOT NULL,
	Success INT NOT NULL CHECK ( Success >= 0),
	PRIMARY KEY (Name),
	FOREIGN KEY (Username) REFERENCES Users (Username)
);
-- Consist of
CREATE TABLE ConsistOf (
	PizzaName VARCHAR,
	IDingredient SERIAL,
	PRIMARY KEY (PizzaName, IDingredient),
	FOREIGN KEY (PizzaName) REFERENCES Pizza (Name),
	FOREIGN KEY (IDingredient) REFERENCES Ingredient (IDingredient)
);
-- Cook
CREATE TABLE Cook (
	Username VARCHAR,
	IDoven SERIAL,
	IDorder SERIAL,
	PRIMARY KEY (Username, IDoven, IDorder),
	FOREIGN KEY (Username) REFERENCES Users (Username),
	FOREIGN KEY (IDoven) REFERENCES Oven (IDoven),
	FOREIGN KEY (IDorder) REFERENCES Orders (IDorder)
);
-- Contain
CREATE TABLE Contain (
	IDorder SERIAL,
	PizzaName VARCHAR,
	Quantity INT NOT NULL CHECK (Quantity > 0),
	PRIMARY KEY (PizzaName, IDorder),
	FOREIGN KEY (PizzaName ) REFERENCES Pizza (Name),
	FOREIGN KEY (IDorder) REFERENCES Orders (IDorder)
);
--SalaryRegister
CREATE TABLE SalaryRegister(
	IDsalary SERIAL NOT NULL,
	Datetime DATE NOT NULL,
	Username VARCHAR NOT NULL,
	Amount NUMERIC NOT NULL CHECK (Amount >=0),
	Iban IBAN NOT NULL,
	FiscalCode FISCALCODE NOT NULL,
	PRIMARY KEY (IDsalary),
	FOREIGN KEY (Username) REFERENCES Users (Username)
);
