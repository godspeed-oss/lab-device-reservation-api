CREATE DATABASE IF NOT EXISTS lab_reservation_db;

USE lab_reservation_db;

DROP TABLE IF EXISTS reservation;
DROP TABLE IF EXISTS device;
DROP TABLE IF EXISTS user;

CREATE TABLE user (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL
);

CREATE TABLE device (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    type VARCHAR(50) NOT NULL,
    status VARCHAR(20) NOT NULL
);

CREATE TABLE reservation (
    id INT PRIMARY KEY AUTO_INCREMENT,
    device_id INT NOT NULL,
    user_id INT NOT NULL,
    user_name VARCHAR(50) NOT NULL,
    reservation_date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL
);

INSERT INTO user (username, password, role) VALUES
('admin', '$2a$10$CwTycUXWue0Thq9StjUM0uJ8cjDRI8n5mzZ5E8bWIYdQ4B1MEQh5K', 'ADMIN'),
('student', '$2a$10$CwTycUXWue0Thq9StjUM0uJ8cjDRI8n5mzZ5E8bWIYdQ4B1MEQh5K', 'USER');

INSERT INTO device (name, type, status) VALUES
('Microscope', 'Optical Equipment', 'Available'),
('3D Printer', 'Manufacturing Equipment', 'Maintenance'),
('Centrifuge', 'Lab Equipment', 'Available'),
('Balance', 'Measurement Equipment', 'Available');