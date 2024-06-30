
package it.polito.med;

public class Doctor {

    String id; 
    String name; 
    String surname;
    String speciality;

    public Doctor(String id, String name, String surname, String speciality){
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.speciality = speciality;
    }

    public void setId(String id){
        this.id = id;
    }
    public String getId(){
        return this.id;
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

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    




}
