package antoniJanson;

import java.util.*;

import antoniJanson.doctor.Doctor;
import antoniJanson.doctor.DoctorService;
import antoniJanson.patient.PatientService;
import antoniJanson.visit.Visit;
import antoniJanson.visit.VisitService;
import lombok.Getter;
import org.apache.commons.codec.digest.DigestUtils;

public class VisitReservation {
    @Getter
    private final static MySQLConnectionFactory connectionFactory = new MySQLConnectionFactory();
    private static final List<String> hoursOfVisits = new ArrayList<>(){{
        add("08:00");
        add("08:30");
        add("09:00");
        add("09:30");
        add("10:00");
        add("10:30");
        add("11:00");
        add("11:30");
        add("12:00");
        add("12:30");
        add("13:00");
        add("13:30");
        add("14:00");
        add("14:30");
        add("15:00");
        add("15:30");
    }};
    private final Scanner sc = new Scanner(System.in);

    private final PatientService patientService = new PatientService();
    private final DoctorService doctorService = new DoctorService();
    private final VisitService visitService = new VisitService();

    public VisitReservation(){}

    public void showAuthMenu(){
        System.out.println("1.Login");
        System.out.println("2.Register");
        String choice = sc.nextLine();
        if(choice.equals("1")){
            showLoginPrompt();
        }else if(choice.equals("2")){
            showRegisterPrompt();
        }
    }

    private void showLoginPrompt() {
        System.out.println("1. Login as patient");
        System.out.println("2. Login as doctor");
        String choice = sc.nextLine();
        if(choice.equals("1")) {
            System.out.println("Provide your username");
            String username = sc.nextLine();
            System.out.println("Provide your password");
            String password = sc.nextLine();
            if (patientService.loginAuthentication(username, password)) {
                showPatientMenu();
            } else {
                System.out.println("Invalid password");
                showAuthMenu();
            }
        }else if(choice.equals("2")){
            System.out.println("Provide your username");
            String username = sc.nextLine();
            System.out.println("Provide your password");
            String password = sc.nextLine();
            if (doctorService.loginAuthentication(username, password)) {
                showDoctorMenu();
            } else {
                System.out.println("Invalid password");
                showAuthMenu();
            }
        }
    }

    private void showDoctorMenu() {
        System.out.println("1. Check schedule");
        System.out.println("2. Make visit reservation for patient");
        System.out.println("3. Logout");
        String choice = sc.nextLine();
        if(choice.equals("1")){
            List<Visit> visitList = visitService.getAllVisitsForDoctorId(doctorService.getCurrentDoctor().getDoctorId());
            if(visitList.size() > 0) {
                showVisits(visitList);
            }else{
                System.out.println("There are no visits in you schedule");
            }
            showDoctorMenu();
        }else if(choice.equals("2")){
            System.out.println("Provide patients id");
            String patientId = sc.nextLine();
            System.out.println("Provide type of visit");
            String type = sc.nextLine();
            System.out.println("Provide date of visit in format YYYY-MM-DD");
            String date = sc.nextLine();
            List<String> acceptableHours = acceptableHours(date, doctorService.getCurrentDoctor().getDoctorId());
            System.out.println("Provide hour of visit in format hh:mm");
            System.out.println("Free hours are: " + acceptableHours);
            String hour = sc.nextLine();
            while(!acceptableHours.contains(hour)){
                System.out.println("You provided hour which is outside of visiting hours or is already taken." +
                        " Please provide correct hour");
                System.out.println("Acceptable hours: " + acceptableHours);
                hour = sc.nextLine();
            }
            visitService.createVisit(doctorService.getCurrentDoctor().getDoctorId(), Integer.parseInt(patientId), type, date, hour);
            showDoctorMenu();
        } else if (choice.equals("3")) {

        }else{
            System.out.println("Bad input!");
            showDoctorMenu();
        }
    }

    private List<String> acceptableHours(String date, int doctorId){
        List<String> acceptableHours = hoursOfVisits;
        List<String> hoursTaken = visitService.getAllVisitsForDoctorId(doctorId).
                stream().filter(e -> e.getDateOfVisit().equals(date)).map(Visit::getTimeOfVisit).toList();
        acceptableHours.removeAll(hoursTaken);
        return acceptableHours;
    }

    private void showVisits(List<Visit> listOfVisits){
        for(Visit visit : listOfVisits){
            System.out.println(visit);
        }
    }

    private void showDoctors(List<Doctor> listOfDoctors){
        for (Doctor doctor : listOfDoctors){
            System.out.println(doctor);
        }
    }

    private Map<Doctor, List<String>> acceptableHoursRegardingSpecialization(String date, String specialization){
        List<Doctor> listOfDoctors = doctorService.getAllDoctorsOfSpecialization(specialization);
        Map<Doctor, List<String>> mapOfDoctorsAndTheirFreeHours = new HashMap<>();
        for(Doctor doctor : listOfDoctors){
            mapOfDoctorsAndTheirFreeHours.put(doctor, acceptableHours(date, doctor.getDoctorId()));
        }
        return mapOfDoctorsAndTheirFreeHours;
    }

    private void showPatientMenu() {
        System.out.println("1. Make visit appointment");
        System.out.println("2. Check reserved visits for patients id");
        System.out.println("3. Change visit time and/or date");
        System.out.println("4. Show doctors of certain specialization");
        System.out.println("5. Show free hours for doctor");
        System.out.println("6. Show free hours for doctors of certain specialization");
        System.out.println("7. Logout");
        String choice = sc.nextLine();
        switch (choice) {
            case "1" -> makeVisit();
            case "2" -> visitsCheck();
            case "3" -> visitModification();
            case "4" -> showDoctorsOfSpecialization();
            case "5" -> freeHoursOfDoctor();
            case "6" -> freeHoursOfDoctorsFromSpecialization();
            case "7" -> {}
            default -> System.out.println("Wrong input");
        }
    }
    private void freeHoursOfDoctorsFromSpecialization(){
        System.out.println("Provide specialization you want to check doctors free hours for");
        String specialization = sc.nextLine();
        System.out.println("Provide date that you are interested in, in format YYYY-MM-DD");
        String date = sc.nextLine();
        System.out.println(acceptableHoursRegardingSpecialization(date, specialization));
        showPatientMenu();
    }
    private void freeHoursOfDoctor(){
        System.out.println("Provide doctor id you want to check hours for");
        String doctorId = sc.nextLine();
        System.out.println("Provide date that you are interested in, in format YYYY-MM-DD");
        String date = sc.nextLine();
        if(doctorService.checkIfDoctorExists(Integer.parseInt(doctorId))){
            System.out.println(acceptableHours(date, Integer.parseInt(doctorId)));
        }else{
            System.out.println("No doctor with such id");
        }
        showPatientMenu();
    }
    private void showDoctorsOfSpecialization(){
        System.out.println("Provide a specialization of doctor you are looking for");
        String specialization = sc.nextLine();
        List<Doctor> doctorList = doctorService.getAllDoctorsOfSpecialization(specialization);
        if(doctorList.size() > 0){
            showDoctors(doctorList);
        }else{
            System.out.println("No doctors with such specialization");
        }
        showPatientMenu();
    }
    private void makeVisit(){
        System.out.println("Provide surname of the doctor you want to visit");
        String surname = sc.nextLine();
        Doctor doctor = doctorService.getDoctorForSurname(surname);
        if(doctor != null){
            System.out.println("Provide the type of visit");
            String type = sc.nextLine();
            System.out.println("Provide the date of your visit in format YYYY-MM-DD: ");
            String date = sc.nextLine();
            List<String> acceptableHoursList = acceptableHours(date, doctor.getDoctorId());
            System.out.println(acceptableHoursList);
            System.out.println("Provide hour of your visit: ");
            String hour = sc.nextLine();
            while(!acceptableHoursList.contains(hour)){
                System.out.println("Please provide correct hour: ");
                hour = sc.nextLine();
            }
            visitService.createVisit(doctor.getDoctorId(), patientService.getCurrentPatient().getPatientId(),
                    type, date, hour);
        }else{
            System.out.println("There is no doctor with such surname");
        }
        showPatientMenu();
    }
    private void visitsCheck(){
        showVisits(visitService.getAllVisitsForPatientId(patientService.getCurrentPatient().getPatientId()));
        showPatientMenu();
    }
    private void visitModification(){
        System.out.println("Give visit id you want to change");
        String visitId = sc.nextLine();
        Visit currentVisit;
        try {
            currentVisit = visitService.getVisitForVisitId(visitId);
            if (currentVisit != null) {
                System.out.println("Your current visit date: " + currentVisit.getDateOfVisit() + ". Time of visit: "
                        + currentVisit.getTimeOfVisit());
                System.out.println("Provide new date of visit in format YYYY-MM-DD: ");
                String newDate = sc.nextLine();
                List<String> acceptableHours = acceptableHours(newDate, currentVisit.getDoctor_id());
                System.out.println("Provide new hour of visit in format hh:mm: ");
                System.out.println(acceptableHours);
                String newHour = sc.nextLine();
                while (!acceptableHours.contains(newHour)) {
                    System.out.println("You provided hour which is outside of visiting hours. Please provide correct hour");
                    System.out.println("Acceptable hours: " + acceptableHours);
                    newHour = sc.nextLine();
                }
                visitService.updateVisit(currentVisit.getVisit_id(), newDate, newHour);
            } else {
                System.out.println("No visit with such id");
            }
        } catch (NumberFormatException e) {
            System.out.println("Wrong visit id format, please provide number");
        }
        showPatientMenu();
    }

    private void showRegisterPrompt() {
        System.out.println("1. Register as patient.");
        System.out.println("2. Register as doctor.");
        String choice = sc.nextLine();
        if(choice.equals("1")){
            System.out.println("Please provide username");
            String username = sc.nextLine();
            while(patientService.checkIfUsernameExists(username)){
                System.out.println("Username is already taken. Please provide new username: ");
                username = sc.nextLine();
            }
            System.out.println("Please provide password");
            String password = sc.nextLine();
            System.out.println("Please provide your name");
            String name = sc.nextLine();
            System.out.println("Please provide your surname");
            String surname = sc.nextLine();
            String pw = DigestUtils.sha256Hex(password);
            patientService.createNewPatient(username, pw, name, surname);
            showAuthMenu();
        }else if(choice.equals("2")){
            System.out.println("Please provide special password to register as a new doctor");
            choice = sc.nextLine();
            String doctorPasswordToRegister = "makapaka";
            if(choice.equals(doctorPasswordToRegister)){
                System.out.println("Please provide your username");
                String username = sc.nextLine();
                while(doctorService.checkIfUsernameExists(username)){
                    System.out.println("Username is already taken. Please provide new username: ");
                    username = sc.nextLine();
                }
                System.out.println("Please provide password");
                String password = sc.nextLine();
                System.out.println("Please provide your name");
                String name = sc.nextLine();
                System.out.println("Please provide your surname");
                String surname = sc.nextLine();
                System.out.println("Please provide your specialization");
                String specialization = sc.nextLine();
                String pw = DigestUtils.sha256Hex(password);
                doctorService.createNewDoctor(username, pw, name,surname, specialization);
                showAuthMenu();
            }else{
                System.out.println("Wrong authentication password to register as doctor");
                showAuthMenu();
            }
        }
    }

}
