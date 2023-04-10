package antoniJanson.doctor;

import antoniJanson.MySQLConnectionFactory;
import antoniJanson.VisitReservation;
import antoniJanson.visit.Visit;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DoctorRepository {

    private final MySQLConnectionFactory connectionFactory = VisitReservation.getConnectionFactory();

    public Optional<Doctor> findByUsername(final String username) {
        try {
            Connection connection = connectionFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM doctors WHERE username = ?");
            statement.setString(1, username);
            System.out.println(statement.toString());
            statement.executeQuery();
            Doctor doctor = null;
            if (statement.getResultSet().next()) {
                doctor = new Doctor(
                        statement.getResultSet().getInt("doctor_id"),
                        statement.getResultSet().getString("username"),
                        statement.getResultSet().getString("password"),
                        statement.getResultSet().getString("name"),
                        statement.getResultSet().getString("surname"),
                        statement.getResultSet().getString("specialization")
                );
            }
            statement.close();
            connection.close();
            return Optional.ofNullable(doctor);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IllegalStateException("Failed to fetch antoniJanson.client");
        }
    }
    public int createDoctor(final String username, final String password, final String name, final String surname, final String specialization) {
        try {
            Connection connection = connectionFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO doctors (name, surname, specialization, username, password)" +
                            " Values(?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, name);
            statement.setString(2, surname);
            statement.setString(3, specialization);
            statement.setString(4, username);
            statement.setString(5, password);
            System.out.println(statement.toString());
            statement.executeUpdate();
            int doctorId = 0;
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    doctorId = keys.getInt(1);
                }else{
                    throw new IllegalStateException("Failed to fetch client_id");
                }
            }
            statement.close();
            connection.close();
            return doctorId;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IllegalStateException("Failed to save antoniJanson.client");
        }
    }

    public List<Doctor> getAllDoctorsOfSpecialization(String specialization) {
        List<Doctor> listOfDoctors = new ArrayList<>();
        try {
            Connection connection = connectionFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT doctor_id, name, surname FROM doctors WHERE specialization = ?");
            statement.setString(1, specialization);
            System.out.println(statement.toString());
            statement.executeQuery();
            while (statement.getResultSet().next()) {
                listOfDoctors.add(new Doctor(
                        statement.getResultSet().getInt("doctor_id"),
                        statement.getResultSet().getString("name"),
                        statement.getResultSet().getString("surname")
                ));
            }
            statement.close();
            connection.close();
            return listOfDoctors;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IllegalStateException("Failed to fetch doctors data");
        }
    }

    public Optional<Object> findById(final int doctorId) {
        try {
            Connection connection = connectionFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM doctors WHERE doctor_id = ?");
            statement.setInt(1, doctorId);
            System.out.println(statement.toString());
            statement.executeQuery();
            Doctor doctor = null;
            if (statement.getResultSet().next()) {
                doctor = new Doctor(
                        statement.getResultSet().getInt("doctor_id"),
                        statement.getResultSet().getString("name"),
                        statement.getResultSet().getString("surname"),
                        statement.getResultSet().getString("specialization"),
                        statement.getResultSet().getString("username"),
                        statement.getResultSet().getString("password")
                );
            }
            statement.close();
            connection.close();
            return Optional.ofNullable(doctor);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IllegalStateException("Failed to fetch antoniJanson.client");
        }
    }

    public Optional<Doctor> getDoctorWithSurname(final String surname) {
        try {
            Connection connection = connectionFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM doctors WHERE surname = ?");
            statement.setString(1, surname);
            System.out.println(statement.toString());
            statement.executeQuery();
            Doctor doctor = null;
            if (statement.getResultSet().next()) {
                doctor = new Doctor(
                        statement.getResultSet().getInt("doctor_id"),
                        statement.getResultSet().getString("username"),
                        statement.getResultSet().getString("password"),
                        statement.getResultSet().getString("name"),
                        statement.getResultSet().getString("surname"),
                        statement.getResultSet().getString("specialization")
                );
            }
            statement.close();
            connection.close();
            return Optional.ofNullable(doctor);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IllegalStateException("Failed to fetch antoniJanson.client");
        }
    }
}
