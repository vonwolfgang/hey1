package it.polito.med;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;


public class MedManager {

	List<String> specialities_list = new ArrayList();
	List<Doctor> doctor_List = new ArrayList();
	SortedMap<String, List<Slot>> schedule_map = new TreeMap<String, List<Slot>>();
	List<Appointment> appointment_list = new ArrayList();
	List<Appointment> accepted_appointment_list = new ArrayList();
	List<Appointment> completed_appointment_list = new ArrayList();

	static int appointment_id = 0;

	/**
	 * add a set of medical specialities to the list of specialities
	 * offered by the med centre.
	 * Method can be invoked multiple times.
	 * Possible duplicates are ignored.
	 * 
	 * @param specialities the specialities
	 */
	public void addSpecialities(String... specialities) {
		
		for (String string : specialities) {

			if (!specialities_list.contains(string)) {
				specialities_list.add(string);	
			}

		}
	}

	/**
	 * retrieves the list of specialities offered in the med centre
	 * 
	 * @return list of specialities
	 */
	public Collection<String> getSpecialities() {
		return specialities_list;
	}
	
	
	/**
	 * adds a new doctor with the list of their specialities
	 * 
	 * @param id		unique id of doctor
	 * @param name		name of doctor
	 * @param surname	surname of doctor
	 * @param speciality speciality of the doctor
	 * @throws MedException in case of duplicate id or non-existing speciality
	 */
	public void addDoctor(String id, String name, String surname, String speciality) throws MedException {
		
		for (Doctor doctor : doctor_List) {
			if (doctor.getId().equals(id)) {
				throw new MedException("Duplicate ID");
			}
		}
		if (!specialities_list.contains(speciality)) {
			throw new MedException("There is no speciality like this");
		}	

		Doctor doc = new Doctor(id, name, surname, speciality);
		doctor_List.add(doc);
		
	}

	/**
	 * retrieves the list of doctors with the given speciality
	 * 
	 * @param speciality required speciality
	 * @return the list of doctor ids
	 */
	public Collection<String> getSpecialists(String speciality) {
		
		List<String> specialist_list_id = new ArrayList();

		for (Doctor doctor : doctor_List) {
			if (doctor.getSpeciality().equals(speciality)) {
				specialist_list_id.add(doctor.getId());
			}
		}	
	
		return specialist_list_id;
	}

	/**
	 * retrieves the name of the doctor with the given code
	 * 
	 * @param code code id of the doctor 
	 * @return the name
	 */
	public String getDocName(String code) {
		
		for (Doctor doctor : doctor_List) {
			if (doctor.getId().equals(code)) {
				return doctor.getName();
			}
		}
		
		return null;
	}

	/**
	 * retrieves the surname of the doctor with the given code
	 * 
	 * @param code code id of the doctor 
	 * @return the surname
	 */
	public String getDocSurname(String code) {

		for (Doctor doctor : doctor_List) {
			if (doctor.getId().equals(code)) {
				return doctor.getSurname();
			}
		}
		
		return null;
	}

	/**
	 * Define a schedule for a doctor on a given day.
	 * Slots are created between start and end hours with a 
	 * duration expressed in minutes.
	 * 
	 * @param code	doctor id code
	 * @param date	date of schedule
	 * @param start	start time
	 * @param end	end time
	 * @param duration duration in minutes
	 * @return the number of slots defined
	 */
	public int addDailySchedule(String code, String date, String start, String end, int duration) {
		
		
		String[] start_time_list = start.split(":");
		int start_time_minute = Integer.parseInt(start_time_list[0]) * 60 + Integer.parseInt(start_time_list[1]);
		
		String[] end_time_list = end.split(":");
		int end_time_minute = Integer.parseInt(end_time_list[0]) * 60 + Integer.parseInt(end_time_list[1]);
		
		int whole_time_minutes = end_time_minute - start_time_minute;
		int number_of_slots = whole_time_minutes / duration;
		
		StringBuilder stb = new StringBuilder();
		for (int i = 0; i < number_of_slots; i++) {
			stb.append("slot-");
			stb.append(i);
			Slot slot = new Slot(stb.toString(), date, start, end, duration);
			schedule_map.get(code).add(slot);
		}
		
		return schedule_map.get(code).size();
	}

	/**
	 * retrieves the available slots available on a given date for a speciality.
	 * The returned map contains an entry for each doctor that has slots scheduled on the date.
	 * The map contains a list of slots described as strings with the format "hh:mm-hh:mm",
	 * e.g. "14:00-14:30" describes a slot starting at 14:00 and lasting 30 minutes.
	 * 
	 * @param date			date to look for
	 * @param speciality	required speciality
	 * @return a map doc-id -> list of slots in the schedule
	 */
	public Map<String, List<String>> findSlots(String date, String speciality) {
		

		List<String> specialist_id_list = (List<String>) getSpecialists(speciality);
		SortedMap<String, List<String>> founded_slots = new TreeMap<String, List<String>>();
		
		for (String id : specialist_id_list) {
			

			if (schedule_map.keySet().contains(id)) {
				
				if (!founded_slots.containsKey(id)) {
					List<String> times = new ArrayList<String>();
					founded_slots.put(id, times);
				}

				for (Slot slot : schedule_map.get(id)) {
					if (slot.getDate().equals(date)) {
						
						String full = slot.getStart_hour() + "-" + slot.getEnd_hour();
						founded_slots.get(id).add(full);
					}
				}
	
			}
		}
		
		
		return founded_slots;
	}

	/**
	 * Define an appointment for a patient in an existing slot of a doctor's schedule
	 * 
	 * @param ssn		ssn of the patient
	 * @param name		name of the patient
	 * @param surname	surname of the patient
	 * @param code		code id of the doctor
	 * @param date		date of the appointment
	 * @param slot		slot to be booked
	 * @return a unique id for the appointment
	 * @throws MedException	in case of invalid code, date or slot
	 */
	public String setAppointment(String ssn, String name, String surname, String code, String date, String slot) throws MedException {
		
		String speciality = "";
		boolean flag = true;
		Doctor doc = null;
		for (Doctor doctor : doctor_List) {
			if (doctor.getId().equals(code)) {
				flag = false;
				speciality = doctor.getSpeciality();
				doc = doctor;
				break;
			}
		}
		if (flag) {
			throw new MedException("No doctor with this code");
		}

		SortedMap<String, List<String>> founded_slots = (SortedMap<String, List<String>>) findSlots(date, speciality);
		if (founded_slots.keySet().size() == 0) {
			throw new MedException("No Slots for this doctor and date");
		}

		Slot found_slot = null;
		for (Slot curr_slot : schedule_map.get(code)) {
			if (curr_slot.getTimeSlot().equals(slot)) {		
				found_slot = curr_slot;
				break;
			}
		}
		if (!flag) {
			throw new MedException("There is no slot with this");
		}

		String app_id = String.valueOf(this.appointment_id);
		
		this.appointment_id++;
		
		Patient patient = new Patient(ssn, name, surname);
		
		Appointment appointment = new Appointment(app_id, doc, patient, date, found_slot);
		appointment_list.add(appointment);
		
		return app_id;
	}

	/**
	 * retrieves the doctor for an appointment
	 * 
	 * @param idAppointment id of appointment
	 * @return doctor code id
	 */
	public String getAppointmentDoctor(String idAppointment) {

		for (Appointment appointment : appointment_list) {
			if (appointment.getAppointment_id().equals(idAppointment)) {
				return appointment.getDoctor().getId();
			}
		}

		return null;
	}

	/**
	 * retrieves the patient for an appointment
	 * 
	 * @param idAppointment id of appointment
	 * @return doctor patient ssn
	 */
	public String getAppointmentPatient(String idAppointment) {
		
		for (Appointment appointment : appointment_list) {
			if (appointment.getAppointment_id().equals(idAppointment)) {
				return appointment.getPatient().getSsn();
			}
		}

		
		return null;
	}

	/**
	 * retrieves the time for an appointment
	 * 
	 * @param idAppointment id of appointment
	 * @return time of appointment
	 */
	public String getAppointmentTime(String idAppointment) {
		
		for (Appointment appointment : appointment_list) {
			if (appointment.getAppointment_id().equals(idAppointment)) {
				return appointment.getTime_slot().getTimeSlot();
			}
		}

		
		
		return null;
	}

	/**
	 * retrieves the date for an appointment
	 * 
	 * @param idAppointment id of appointment
	 * @return date
	 */
	public String getAppointmentDate(String idAppointment) {
		
		for (Appointment appointment : appointment_list) {
			if (appointment.getAppointment_id().equals(idAppointment)) {
				return appointment.getDate();
			}
		}

		
		return null;
	}

	/**
	 * retrieves the list of a doctor appointments for a given day.
	 * Appointments are reported as string with the format
	 * "hh:mm=SSN"
	 * 
	 * @param code doctor id
	 * @param date date required
	 * @return list of appointments
	 */
	public Collection<String> listAppointments(String code, String date) {
		
		List<String> ssn_time_list = new ArrayList();

		for (Appointment appointment : appointment_list) {
			if (appointment.getDoctor().getId().equals(code) && appointment.getDate().equals(date)) {
			
				String start_hour = appointment.getTime_slot().getStart_hour();
				String ssn = appointment.getPatient().getSsn();
				ssn_time_list.add(start_hour + "=" + ssn);
			}
		}

		
		
		return ssn_time_list;
	}

	/**
	 * Define the current date for the medical centre
	 * The date will be used to accept patients arriving at the centre.
	 * 
	 * @param date	current date
	 * @return the number of total appointments for the day
	 */
	public int setCurrentDate(String date) {
		

		int num = 0;
		for (Appointment appointment : appointment_list) {
			if (appointment.getDate().equals(date)) {
				num++;
			}
		}
		
		return num;
	}

	/**
	 * mark the patient as accepted by the med centre reception
	 * 
	 * @param ssn SSN of the patient
	 */
	public void accept(String ssn) {

		for (Appointment appointment : appointment_list) {
			if (appointment.getPatient().getSsn().equals(ssn)) {
				accepted_appointment_list.add(appointment);
				break;
			}
		}

	}

	/**
	 * returns the next appointment of a patient that has been accepted.
	 * Returns the id of the earliest appointment whose patient has been
	 * accepted and the appointment not completed yet.
	 * Returns null if no such appointment is available.
	 * 
	 * @param code	code id of the doctor
	 * @return appointment id
	 */
	public String nextAppointment(String code) {

		List<Appointment> appointment_list_for_code = new ArrayList();

		for (Appointment appointment : appointment_list) {
			
			if (appointment.getDoctor().getId().equals(code)) {
				appointment_list_for_code.add(appointment);		
			}
		}

		Collections.sort(appointment_list_for_code, new AppointmentByDate());

		for (Appointment appointment : appointment_list_for_code) {
			if (!completed_appointment_list.contains(appointment) && accepted_appointment_list.contains(appointment)) {
				return appointment.getAppointment_id();
			}
		}


		return null;
	}

	/**
	 * mark an appointment as complete.
	 * The appointment must be with the doctor with the given code
	 * the patient must have been accepted
	 * 
	 * @param code		doctor code id
	 * @param appId		appointment id
	 * @throws MedException in case code or appointment code not valid,
	 * 						or appointment with another doctor
	 * 						or patient not accepted
	 * 						or appointment not for the current day
	 */
	public void completeAppointment(String code, String appId)  throws MedException {

		for (Appointment appointment : appointment_list) {
			if (appointment.getDoctor().getId().equals(code) && appointment.getAppointment_id().equals(appId)) {
				completed_appointment_list.add(appointment);
			}
		}
	}

	/**
	 * computes the show rate for the appointments of a doctor on a given date.
	 * The rate is the ratio of accepted patients over the number of appointments
	 *  
	 * @param code		doctor id
	 * @param date		reference date
	 * @return	no show rate
	 */
	public double showRate(String code, String date) {

		double accepted = 0;
		double total = 0;

		for (Appointment appointment : accepted_appointment_list) {
			if (appointment.getDoctor().getId().equals(code) && appointment.getDate().equals(date)) {
				accepted++;
			}
		}

		for (Appointment appointment : appointment_list) {
			if (appointment.getDoctor().getId().equals(code) && appointment.getDate().equals(date)) {
				total++;
			}
		}

		
		return accepted/total;
	}

	/**
	 * computes the schedule completeness for all doctors of the med centre.
	 * The completeness for a doctor is the ratio of the number of appointments
	 * over the number of slots in the schedule.
	 * The result is a map that associates to each doctor id the relative completeness
	 * 
	 * @return the map id : completeness
	 */
	public Map<String, Double> scheduleCompleteness() {

		double number_of_slots = 0;
		double number_of_appointment = 0;
		
		SortedMap<String, Double> doctor_completeness = new TreeMap();
		
		for (Doctor doctor : doctor_List) {
			
			number_of_slots = schedule_map.get(doctor.getId()).size();

			for (Appointment appointment : appointment_list) {
				if (appointment.getDoctor().getId().equals(doctor.getId())) {
					number_of_appointment++;
				}
			}

			doctor_completeness.put(doctor.getId(), number_of_appointment/number_of_slots);


		}

		return doctor_completeness;
	}


	
}
