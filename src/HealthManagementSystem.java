import java.sql.*;
import java.util.*;

class Patient {
    private String name;
    private int age;
    private String ailment;
    private List<Appointment> appointmentHistory = new ArrayList<>();
    private Map<Appointment, String> appointmentHistoryWithStatus = new HashMap<>();

    public Patient(String name, int age, String ailment) {
        this.name = name;
        this.age = age;
        this.ailment = ailment;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getAilment() {
        return ailment;
    }

    public List<Appointment> getAppointmentHistory() {
        return appointmentHistory;
    }

    public Map<Appointment, String> getAppointmentHistoryWithStatus() {
        return appointmentHistoryWithStatus;
    }

    public void addAppointmentToHistory(Appointment appointment) {
        appointmentHistory.add(appointment);
    }

    public void addAppointmentToHistoryWithStatus(Appointment appointment, String status) {
        appointmentHistoryWithStatus.put(appointment, status);
    }

    @Override
    public String toString() {
        return "Patient Name: " + name + ", Age: " + age + ", Ailment: " + ailment;
    }
}

class Doctor {
    private String name;
    private String specialization;

    public Doctor(String name, String specialization) {
        this.name = name;
        this.specialization = specialization;
    }

    public String getName() {
        return name;
    }

    public String getSpecialization() {
        return specialization;
    }

    @Override
    public String toString() {
        return "Doctor Name: " + name + ", Specialization: " + specialization;
    }
}

class Appointment {
    private Patient patient;
    private Doctor doctor;
    private String date;
    private boolean isRecurring;

    public Appointment(Patient patient, Doctor doctor, String date, boolean isRecurring) {
        this.patient = patient;
        this.doctor = doctor;
        this.date = date;
        this.isRecurring = isRecurring;
    }

    public Patient getPatient() {
        return patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isRecurring() {
        return isRecurring;
    }

    @Override
    public String toString() {
        return "Appointment Details:\n" + patient + "\n" + doctor + "\nDate: " + date;
    }
}

public class HealthManagementSystem {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/health_management";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "password";

    private static Connection connection;

    public static void main(String[] args) {
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            initializeDatabase();

            Scanner scanner = new Scanner(System.in);
            System.out.println("Welcome to the Healthcare Management System!");

            while (true) {
                System.out.println("\nChoose an option:");
                System.out.println("1. View Doctors");
                System.out.println("2. Book an Appointment");
                System.out.println("3. View Appointments");
                System.out.println("4. Cancel an Appointment");
                System.out.println("5. Reschedule an Appointment");
                System.out.println("6. Exit");

                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        viewDoctors();
                        break;
                    case 2:
                        bookAppointment(scanner);
                        break;
                    case 3:
                        viewAppointments();
                        break;
                    case 4:
                        cancelAppointment(scanner);
                        break;
                    case 5:
                        rescheduleAppointment(scanner);
                        break;
                    case 6:
                        System.out.println("Exiting the program. Stay healthy!");
                        scanner.close();
                        connection.close();
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void initializeDatabase() {
        try (Statement stmt = connection.createStatement()) {
            String createDoctorsTable = "CREATE TABLE IF NOT EXISTS doctors (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "name VARCHAR(100), " +
                    "specialization VARCHAR(100));";
            stmt.execute(createDoctorsTable);

            String createPatientsTable = "CREATE TABLE IF NOT EXISTS patients (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "name VARCHAR(100), " +
                    "age INT, " +
                    "ailment VARCHAR(255));";
            stmt.execute(createPatientsTable);

            String createAppointmentsTable = "CREATE TABLE IF NOT EXISTS appointments (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "patient_id INT, " +
                    "doctor_id INT, " +
                    "date DATE, " +
                    "is_recurring BOOLEAN, " +
                    "FOREIGN KEY (patient_id) REFERENCES patients(id), " +
                    "FOREIGN KEY (doctor_id) REFERENCES doctors(id));";
            stmt.execute(createAppointmentsTable);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void viewDoctors() {
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM doctors")) {

            System.out.println("\nAvailable Doctors:");
            while (rs.next()) {
                System.out.println(rs.getInt("id") + ". Doctor Name: " + rs.getString("name") + ", Specialization: " + rs.getString("specialization"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void bookAppointment(Scanner scanner) {
        try {
            System.out.println("\nEnter patient details:");
            System.out.print("Name: ");
            String name = scanner.nextLine();
            System.out.print("Age: ");
            int age = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Ailment: ");
            String ailment = scanner.nextLine();

            int patientId = addOrGetPatient(name, age, ailment);

            System.out.println("\nSelect a doctor by ID:");
            viewDoctors();

            System.out.print("Enter doctor ID: ");
            int doctorId = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Enter appointment date (YYYY-MM-DD): ");
            String date = scanner.nextLine();

            System.out.print("Is this a recurring appointment? (yes/no): ");
            boolean isRecurring = scanner.nextLine().equalsIgnoreCase("yes");

            String insertAppointment = "INSERT INTO appointments (patient_id, doctor_id, date, is_recurring) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstmt = connection.prepareStatement(insertAppointment)) {
                pstmt.setInt(1, patientId);
                pstmt.setInt(2, doctorId);
                pstmt.setDate(3, Date.valueOf(date));
                pstmt.setBoolean(4, isRecurring);
                pstmt.executeUpdate();

                System.out.println("Appointment booked successfully!");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static int addOrGetPatient(String name, int age, String ailment) throws SQLException {
        String checkPatient = "SELECT id FROM patients WHERE name = ? AND age = ? AND ailment = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(checkPatient)) {
            pstmt.setString(1, name);
            pstmt.setInt(2, age);
            pstmt.setString(3, ailment);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        }

        String insertPatient = "INSERT INTO patients (name, age, ailment) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(insertPatient, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, name);
            pstmt.setInt(2, age);
            pstmt.setString(3, ailment);
            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        throw new SQLException("Failed to add or retrieve patient.");
    }

    private static void viewAppointments() {
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT a.id, p.name AS patient_name, d.name AS doctor_name, a.date, a.is_recurring " +
                     "FROM appointments a " +
                     "JOIN patients p ON a.patient_id = p.id " +
                     "JOIN doctors d ON a.doctor_id = d.id")) {

            System.out.println("\nScheduled Appointments:");
            while (rs.next()) {
                System.out.println("Appointment ID: " + rs.getInt("id") +
                        ", Patient: " + rs.getString("patient_name") +
                        ", Doctor: " + rs.getString("doctor_name") +
                        ", Date: " + rs.getDate("date") +
                        ", Recurring: " + rs.getBoolean("is_recurring"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void cancelAppointment(Scanner scanner) {
        try {
            System.out.print("Enter appointment ID to cancel: ");
            int appointmentId = scanner.nextInt();

            String deleteAppointment = "DELETE FROM appointments WHERE id = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(deleteAppointment)) {
                pstmt.setInt(1, appointmentId);
                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Appointment cancelled successfully.");
                } else {
                    System.out.println("No appointment found with the given ID.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void rescheduleAppointment(Scanner scanner) {
        try {
            System.out.print("Enter appointment ID to reschedule: ");
            int appointmentId = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Enter new appointment date (YYYY-MM-DD): ");
            String newDate = scanner.nextLine();

            String updateAppointment = "UPDATE appointments SET date = ? WHERE id = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(updateAppointment)) {
                pstmt.setDate(1, Date.valueOf(newDate));
                pstmt.setInt(2, appointmentId);
                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Appointment rescheduled successfully.");
                } else {
                    System.out.println("No appointment found with the given ID.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
