
CREATE DATABASE IF NOT EXISTS HealthManagementSystem;


USE HealthManagementSystem;

CREATE TABLE patients (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    age INT NOT NULL,
    ailment VARCHAR(255)
);


CREATE TABLE doctors (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    specialization VARCHAR(255) NOT NULL
);

INSERT INTO doctors (name, specialization) VALUES
    ('Dr. Alice Smith', 'Pediatrics'),
    ('Dr. Bob Johnson', 'Cardiology'),
    ('Dr. Carol Davis', 'Neurology'),
    ('Dr. Lucas Bennett', 'Orthopedic Surgery'),
    ('Dr. Elijah Khan', 'Psychiatry'),
    ('Dr. Mason Carter', 'General Medicine'),
    ('Dr. William Adams', 'Pulmonology'),
    ('Dr. Lily Morgan', 'Infectious Disease'),
    ('Dr. Alexander Lee', 'Urology'),
    ('Dr. Charlotte Brooks', 'Obstetrics and Gynecology');

CREATE TABLE appointments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    patient_id INT NOT NULL,
    doctor_id INT NOT NULL,
    date DATE NOT NULL,
    is_recurring BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (patient_id) REFERENCES patients(id) ON DELETE CASCADE,
    FOREIGN KEY (doctor_id) REFERENCES doctors(id) ON DELETE CASCADE
);
