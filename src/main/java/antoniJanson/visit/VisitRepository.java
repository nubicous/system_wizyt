package antoniJanson.visit;

import antoniJanson.MySQLConnectionFactory;
import antoniJanson.VisitReservation;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class VisitRepository {

    private final MySQLConnectionFactory connectionFactory = VisitReservation.getConnectionFactory();
    public List<Visit> getVisitsForDoctor(final int currentDoctorId) {
        List<Visit> visits = new ArrayList<>();
        String pattern = "HH:mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        try {
            Connection connection = connectionFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM visits WHERE doctor_id = ?");
            statement.setInt(1, currentDoctorId);
            statement.executeQuery();
            while (statement.getResultSet().next()) {
                visits.add(new Visit(
                        statement.getResultSet().getInt("visit_id"),
                        statement.getResultSet().getInt("doctor_id"),
                        statement.getResultSet().getInt("patient_id"),
                        statement.getResultSet().getString("visit_type"),
                        statement.getResultSet().getDate("date_of_visit").toString(),
                        simpleDateFormat.format(statement.getResultSet().getTime("time_of_visit"))
                ));
            }
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IllegalStateException("Failed to fetch visits for doctor");
        }
        return visits;
    }

    public List<Visit> getVisitsForPatient(final int currentPatientId) {
        List<Visit> visits = new ArrayList<>();
        String pattern = "HH:mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        try {
            Connection connection = connectionFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM visits WHERE patient_id = ?");
            statement.setInt(1, currentPatientId);
            statement.executeQuery();
            while (statement.getResultSet().next()) {
                visits.add(new Visit(
                        statement.getResultSet().getInt("visit_id"),
                        statement.getResultSet().getInt("doctor_id"),
                        statement.getResultSet().getInt("patient_id"),
                        statement.getResultSet().getString("visit_type"),
                        statement.getResultSet().getDate("date_of_visit").toString(),
                        simpleDateFormat.format(statement.getResultSet().getTime("time_of_visit"))
                ));
            }
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IllegalStateException("Failed to fetch visits for patient");
        }
        return visits;
    }

    void addVisit(final int currentDoctorId, final int patientId, final String visitType, final String date, final String time) {
        try {
            Connection connection = connectionFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO visits (patient_id, doctor_id, visit_type, date_of_visit, time_of_visit) " +
                            "VALUES (?, ?, ?, ?, ?)");
            statement.setInt(1, patientId);
            statement.setInt(2, currentDoctorId);
            statement.setString(3, visitType);
            statement.setString(4, date);
            statement.setString(5, time);
            statement.executeUpdate();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IllegalStateException("Failed to place add new visit!");
        }
    }

    public Visit getVisitForVisitId(final int visitId) {
        String pattern = "HH:mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        try {
            Connection connection = connectionFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM visits WHERE visit_id = ?");
            statement.setInt(1, visitId);
            statement.executeQuery();
            Visit visit = null;
            if (statement.getResultSet().next()) {
                visit = new Visit(
                        statement.getResultSet().getInt("visit_id"),
                        statement.getResultSet().getInt("doctor_id"),
                        statement.getResultSet().getInt("patient_id"),
                        statement.getResultSet().getString("visit_type"),
                        statement.getResultSet().getDate("date_of_visit").toString(),
                        simpleDateFormat.format(statement.getResultSet().getTime("time_of_visit"))
                );
            }
            statement.close();
            connection.close();
            return visit;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IllegalStateException("Failed to fetch visits for patient");
        }
    }

    public void updateVisitDateAndHour(int visitId, String date, String hour) {
        try {
            Connection connection = connectionFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "Update visits set date_of_visit = ?, time_of_visit = ? WHERE visit_id = ?");
            statement.setString(1, date);
            statement.setString(2, hour);
            statement.setInt(3, visitId);
            statement.executeUpdate();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IllegalStateException("Failed to update visit hour and date");
        }
    }

    public List<Visit> getVisitsForDoctorsWithSpecialization(final String specialization) {
        List<Visit> visits = new ArrayList<>();
        String pattern = "HH:mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        try {
            Connection connection = connectionFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM visits WHERE specialization = ?");
            statement.setString(1, specialization);
            statement.executeQuery();
            while (statement.getResultSet().next()) {
                visits.add(new Visit(
                        statement.getResultSet().getInt("visit_id"),
                        statement.getResultSet().getInt("doctor_id"),
                        statement.getResultSet().getInt("patient_id"),
                        statement.getResultSet().getString("visit_type"),
                        statement.getResultSet().getDate("date_of_visit").toString(),
                        simpleDateFormat.format(statement.getResultSet().getTime("time_of_visit"))
                ));
            }
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IllegalStateException("Failed to fetch visits for doctor");
        }
        return visits;
    }
}
