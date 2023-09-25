-- Drop the existing database if needed
DROP DATABASE IF EXISTS realestate;

-- Create a new database
CREATE DATABASE realestate;

-- Use the newly created database
USE realestate;

-- Create the Role table with snake_case column names
CREATE TABLE Role (
    id INT PRIMARY KEY AUTO_INCREMENT,
    role_name VARCHAR(255) NOT NULL
);

-- Create the User table with snake_case column names
CREATE TABLE User (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role_id INT,
    fullname VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone VARCHAR(20) UNIQUE,
    avatar TEXT,
    gender ENUM('Male', 'Female', 'Other'),
    hire_date TIMESTAMP,
    address TEXT,
    is_active BOOLEAN DEFAULT TRUE,
	date_of_birth TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (role_id) REFERENCES Role(id)
);

-- Create the Customer table with snake_case column names
CREATE TABLE Customer (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    fullname VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone VARCHAR(20) UNIQUE,
    date_of_birth TIMESTAMP,
    gender ENUM('Male', 'Female', 'Other'),
    address TEXT,
    avatar TEXT,
    times INT default 1, 
    occupation VARCHAR(255),
    is_active BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);



-- Create the ServicePackage table with snake_case column names
CREATE TABLE Package (
    id INT PRIMARY KEY AUTO_INCREMENT,
    package_name VARCHAR(255) NOT NULL,
    times INT default 1, 
    description TEXT,
    price DECIMAL(10, 2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create the CustomerServiceRegistration table with snake_case column names
CREATE TABLE CustomerPackageRegistration (
    id INT PRIMARY KEY AUTO_INCREMENT,
    customer_id INT,
    package_id INT,
    quantity INT DEFAULT 0,
    registration_date  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY unique_customer_service (id),
    FOREIGN KEY (customer_id) REFERENCES Customer(id),
    FOREIGN KEY (package_id) REFERENCES Package(id)
);

-- Create the Location table with snake_case column names
CREATE TABLE Location (
    id INT PRIMARY KEY AUTO_INCREMENT,
    city VARCHAR(255) NOT NULL,
    district VARCHAR(255),
    street VARCHAR(255)
);

-- Create the Category table with snake_case column names
CREATE TABLE Category (
    id INT PRIMARY KEY AUTO_INCREMENT,
    category_name VARCHAR(255) NOT NULL
);

-- Create the Property table with snake_case column names
CREATE TABLE Property (
    id INT PRIMARY KEY AUTO_INCREMENT,
    property_name VARCHAR(255) NOT NULL,
    is_active boolean default false,
    location_id INT UNIQUE,
    category_id INT,
    customer_id INT,
    price DECIMAL(10, 2),
    description TEXT,
    is_deleted boolean default false,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES Customer(id),
    FOREIGN KEY (location_id) REFERENCES Location(id),
    FOREIGN KEY (category_id) REFERENCES Category(id)
);

-- Create the ManageProperty table with snake_case column names
CREATE TABLE ManageProperty (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    property_id INT,
    FOREIGN KEY (user_id) REFERENCES User(id),
    FOREIGN KEY (property_id) REFERENCES Property(id)
);

-- Create the Transaction table with snake_case column names
CREATE TABLE Transaction (
    id INT PRIMARY KEY AUTO_INCREMENT,
    property_id INT,
    customer_id INT,
    transaction_date TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (property_id) REFERENCES Property(id),
    FOREIGN KEY (customer_id) REFERENCES Customer(id)
);

-- Create the Blog table with snake_case column names
CREATE TABLE Blog (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    title VARCHAR(255) NOT NULL,
    content TEXT,
    is_publish boolean default 0,
    publish_date TIMESTAMP ,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES User(id)
);

-- Create the Documents table with snake_case column names
CREATE TABLE Documents (
    id INT PRIMARY KEY AUTO_INCREMENT,
    document_name VARCHAR(255) NOT NULL,
    file_path VARCHAR(255) NOT NULL,
    property_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (property_id) REFERENCES Property(id)
);

-- Create the Comment table with snake_case column names
CREATE TABLE Comment (
    id INT PRIMARY KEY AUTO_INCREMENT,
    customer_id INT,
    property_id INT,
    comment TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES customer(id),
    FOREIGN KEY (property_id) REFERENCES Property(id)
);

-- Create the Review table with snake_case column names
CREATE TABLE Review (
    id INT PRIMARY KEY AUTO_INCREMENT,
    customer_id INT,
    user_id INT,
    rating INT,
    review_text TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES Customer(id),
    FOREIGN KEY (user_id) REFERENCES User(id)
);

-- Create the Wishlist table with snake_case column names
CREATE TABLE Wishlist (
	id INT PRIMARY KEY AUTO_INCREMENT,
    customer_id INT,
    property_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES Customer(id),
    FOREIGN KEY (property_id) REFERENCES Property(id)
);


-- Create the Appointment table with snake_case column names
CREATE TABLE Appointment (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    customer_id INT,
    property_id INT,
    appointment_date TIMESTAMP,
    is_active BOOLEAN,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES User(id),
    FOREIGN KEY (customer_id) REFERENCES Customer(id),
    FOREIGN KEY (property_id) REFERENCES Property(id)
);

-- Create the NotificationContent table with snake_case column names
CREATE TABLE NotificationContent (
    id INT PRIMARY KEY AUTO_INCREMENT,
    subject VARCHAR(255) NOT NULL,
    message TEXT,
    attachment_path VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create the Notification table with snake_case column names
CREATE TABLE Notification (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    notification_content_id INT,
    is_active BOOLEAN,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES User(id),
    FOREIGN KEY (notification_content_id) REFERENCES NotificationContent(id)
);

-- Create the Media table with snake_case column names
CREATE TABLE Media (
    id INT PRIMARY KEY AUTO_INCREMENT,
    media_name VARCHAR(255) NOT NULL,
    media_type VARCHAR(50),
    property_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (property_id) REFERENCES Property(id)
);

-- Create the Payment table with snake_case column names
CREATE TABLE Payment (
    id INT PRIMARY KEY AUTO_INCREMENT,
    customerpackageregistration_id INT,
    amount DECIMAL(10, 2) NOT NULL,
    payment_date TIMESTAMP NOT NULL,
    payment_method VARCHAR(255),
    payment_status VARCHAR(255) DEFAULT "DEACTIVE",
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (customerpackageregistration_id) REFERENCES CustomerPackageRegistration(id)
);


-- Insert data

-- Role
INSERT INTO `realestate`.`role` (`id`,`role_name`) VALUES (1,'ROLE_ADMIN');
INSERT INTO `realestate`.`role`(`id`,`role_name`)  VALUES (2, 'ROLE_STAFF');

-- User

INSERT INTO `realestate`.`user` (`Username`, `Password`, `Role_ID`, `Fullname`, `Email`, `Phone`) VALUES ('staff', '$2a$10$ktndSh0vIJ4B2jdV82ea8.bv2CPjpMU28hf2oNSfGOxIaLeXfXnjS', '2', 'Đình', 'chudev@gmail.com', '0334436231');
INSERT INTO `realestate`.`user` (`Username`, `Password`, `Role_ID`, `FullName`, `Email`, `Phone`) VALUES ('admin', '$2a$10$ktndSh0vIJ4B2jdV82ea8.bv2CPjpMU28hf2oNSfGOxIaLeXfXnjS', '1', 'Hảo', 'ab@gmail.com', '0334436232');

-- Customer
INSERT INTO `realestate`.`customer` (`Username`, `Password`, `FullName`, `Email`, `Phone`) VALUES ('chuongdp', '$2a$10$ktndSh0vIJ4B2jdV82ea8.bv2CPjpMU28hf2oNSfGOxIaLeXfXnjS', 'Đình Chương', 'chu@gmail.com', '0334436233');


-- Category
INSERT INTO `realestate`.`category` (`category_name`) VALUES ('Nhà thuê');

-- Package

INSERT INTO `realestate`.`package` (`package_name`, `times`, `price`) VALUES ('GÓI 1', '1', '100');
INSERT INTO `realestate`.`package` (`package_name`, `times`, `price`) VALUES ('GÓI 2', '2', '200');
INSERT INTO `realestate`.`package` (`package_name`, `times`, `price`) VALUES ('GÓI 3', '3', '300');
