package it.polito.med;

public class Appointment {

    String appointment_id;
    Doctor doctor;
    Patient patient;
    String date;
    Slot slot;
    public Appointment(String appointment_id, Doctor doctor, Patient patient, String date, Slot slot) {
        this.appointment_id = appointment_id;
        this.doctor = doctor;
        this.patient = patient;
        this.date = date;
        this.slot = slot;
    }
    public String getAppointment_id() {
        return appointment_id;
    }
    public void setAppointment_id(String appointment_id) {
        this.appointment_id = appointment_id;
    }
    public Doctor getDoctor() {
        return doctor;
    }
    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }
    public Patient getPatient() {
        return patient;
    }
    public void setPatient(Patient patient) {
        this.patient = patient;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public Slot getTime_slot() {
        return slot;
    }
    public void setTime_slot(Slot slot) {
        this.slot = slot;
    }

    
}
