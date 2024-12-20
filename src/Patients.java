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

