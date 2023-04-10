package antoniJanson.visit;

import java.util.List;

public class VisitService {
    private final VisitRepository visitRepository = new VisitRepository();

    public List<Visit> getAllVisitsForDoctorId(int currentDoctorId){
        return visitRepository.getVisitsForDoctor(currentDoctorId);
    }

    public List<Visit> getAllVisitsForDoctorsWithSpecialization(String specialization){
        return visitRepository.getVisitsForDoctorsWithSpecialization(specialization);
    }

    public List<Visit> getAllVisitsForPatientId(int currentPatientId){
        return visitRepository.getVisitsForPatient(currentPatientId);
    }

    public Visit getVisitForVisitId(String visitId){
        return visitRepository.getVisitForVisitId(Integer.parseInt(visitId));
    }

    public void createVisit(int currentDoctorId, int patientId,String visitType, String date, String time){
        visitRepository.addVisit(currentDoctorId, patientId, visitType, date, time);
    }

    public void updateVisit(int visitId, String date, String hour) {
        visitRepository.updateVisitDateAndHour(visitId, date, hour);
    }
}
