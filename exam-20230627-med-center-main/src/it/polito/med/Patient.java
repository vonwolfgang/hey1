package it.polito.med;

public class Patient {
    
    String ssn;
    String name; 
    String surname;
    
    public Patient(String ssn, String name, String surname) {
        this.ssn = ssn;
        this.name = name;
        this.surname = surname;
    }
    public String getSsn() {
        return ssn;
    }
    public void setSsn(String ssn) {
        this.ssn = ssn;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getSurname() {
        return surname;
    }
    public void setSurname(String surname) {
        this.surname = surname;
    }

    

}
