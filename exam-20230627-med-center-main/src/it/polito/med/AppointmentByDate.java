package it.polito.med;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class AppointmentByDate implements Comparator<Appointment> {

    @Override
    public int compare(Appointment o1, Appointment o2) {
        
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy"); 

        Date o1Date=null;
        Date o2Date=null;
        try {
            o1Date = df.parse(o1.getDate());
            o2Date = df.parse(o2.getDate());

        } 
        catch (ParseException e) {
            e.printStackTrace();
        }
        
        return o1Date.compareTo(o2Date); 
    
    }

}
