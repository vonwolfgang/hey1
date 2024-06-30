package it.polito.med;


public class Slot {

    String name;
    String date;
    String start_hour;
    String end_hour;
    int duration;
    public Slot(String name, String date, String start_hour, String end_hour, int duration) {
        this.name = name;
        this.date = date;
        this.start_hour = start_hour;
        this.end_hour = end_hour;
        this.duration = duration;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getStart_hour() {
        return start_hour;
    }
    public void setStart_hour(String start_hour) {
        this.start_hour = start_hour;
    }
    public String getEnd_hour() {
        return end_hour;
    }
    public void setEnd_hour(String end_hour) {
        this.end_hour = end_hour;
    }
    public int getDuration() {
        return duration;
    }
    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getTimeSlot(){
        return getStart_hour() + "-" + getEnd_hour();
    }
    
    
}
