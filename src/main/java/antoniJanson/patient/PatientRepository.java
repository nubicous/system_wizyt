package antoniJanson.patient;

import antoniJanson.MySQLConnectionFactory;
import antoniJanson.VisitReservation;

import java.sql.*;
import java.util.Optional;

public class PatientRepository {

    private final MySQLConnectionFactory connectionFactory = VisitReservation.getConnectionFactory();
    public Optional<Patient> findByUsername(String username){
        try {
            Connection connection = connectionFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM patients WHERE username = ?");
            statement.setString(1, username);
            System.out.println(statement.toString());
            statement.executeQuery();
            Patient patient = null;
            if (statement.getResultSet().next()) {
                patient = new Patient(
                        statement.getResultSet().getInt("patient_id"),
                        statement.getResultSet().getString("username"),
                        statement.getResultSet().getString("password"),
                        statement.getResultSet().getString("name"),
                        statement.getResultSet().getString("surname")
                );
            }
            statement.close();
            connection.close();
            return Optional.ofNullable(patient);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IllegalStateException("Failed to fetch antoniJanson.client");
        }
    }

    public int createPatient(final String username, final String password, final String name, final String surname) {
        try {
            Connection connection = connectionFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO patients (name, surname, username, password)" +
                            " Values(?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, name);
            statement.setString(2, surname);
            statement.setString(3, username);
            statement.setString(4, password);
            System.out.println(statement.toString());
            statement.executeUpdate();
            int patientId = 0;
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    patientId = keys.getInt(1);
                }else{
                    throw new IllegalStateException("Failed to fetch client_id");
                }
            }
            statement.close();
            connection.close();
            return patientId;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IllegalStateException("Failed to save antoniJanson.client");
        }
    }
}
