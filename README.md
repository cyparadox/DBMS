# Healthcare Management System

## Overview
The Healthcare Management System is a Java-based application designed to simplify the management of patient appointments. It integrates with MySQL/MariaDB to store and retrieve data about doctors, patients, and appointments.

## Features
- **Doctor Management**: View the list of available doctors and their specializations.
- **Patient Management**: Automatically register patient details when booking appointments.
- **Appointment Management**: Book, view, cancel, and reschedule appointments, including recurring ones.

## Database Integration
The application uses a relational database with the following schema:

- **Doctors**:
  - `id`: Auto-incremented primary key.
  - `name`: Doctor's name.
  - `specialization`: Doctor's area of expertise.

- **Patients**:
  - `id`: Auto-incremented primary key.
  - `name`: Patient's name.
  - `age`: Patient's age.
  - `ailment`: Description of the patient's ailment.

- **Appointments**:
  - `id`: Auto-incremented primary key.
  - `patient_id`: Foreign key referencing `patients(id)`.
  - `doctor_id`: Foreign key referencing `doctors(id)`.
  - `date`: Date of the appointment.
  - `is_recurring`: Boolean indicating if the appointment recurs.

To set up the database, run the following SQL commands:
```sql
CREATE DATABASE IF NOT EXISTS health_management;
USE health_management;

CREATE TABLE IF NOT EXISTS doctors (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    specialization VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS patients (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    age INT,
    ailment VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS appointments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    patient_id INT,
    doctor_id INT,
    date DATE,
    is_recurring BOOLEAN,
    FOREIGN KEY (patient_id) REFERENCES patients(id),
    FOREIGN KEY (doctor_id) REFERENCES doctors(id)
);
```

## How to Run
1. **Set Up the Database**:
   - Ensure MySQL/MariaDB is running.
   - Run the provided SQL commands to create the database and tables.

2. **Configure the Program**:
   - Update the `DB_URL`, `DB_USER`, and `DB_PASSWORD` variables in the code with your database credentials.

3. **Compile and Run the Program**:
   - Use the command line or an IDE to compile and run the program:
     ```bash
     javac HealthManagementSystem.java
     java HealthManagementSystem
     ```

4. **Interact with the Application**:
   - Choose options from the menu to manage doctors, patients, and appointments.

