package antoniJanson.patient;

import lombok.Getter;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Objects;
import java.util.Optional;

public class PatientService {

    @Getter
    private Patient currentPatient;
    private final PatientRepository patientRepository = new PatientRepository();


    public boolean checkIfUsernameExists(final String username) {
        return patientRepository.findByUsername(username).isPresent();
    }

    public boolean loginAuthentication(String username, String password){
        Optional<Patient> patient = patientRepository.findByUsername(username);
        if(patient.isPresent()){
            currentPatient = patient.get();
            return Objects.equals(currentPatient.getPassword(), encryptPass(password));
        }
        return false;
    }

    public Patient createNewPatient(String username, String password, String name, String surname) {
        return new Patient(patientRepository.createPatient(username, password, name, surname),
                username, password, name ,surname);
    }

    private String encryptPass(String password) {
        try {
            //retrieve instance of the encryptor of SHA-256
            MessageDigest digestor = MessageDigest.getInstance("SHA-256");
            //retrieve bytes to encrypt
            byte[] encodedhash = digestor.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder encryptionValue = new StringBuilder(2 * encodedhash.length);
            //perform encryption
            for (int i = 0; i < encodedhash.length; i++) {
                String hexVal = Integer.toHexString(0xff & encodedhash[i]);
                if (hexVal.length() == 1) {
                    encryptionValue.append('0');
                }
                encryptionValue.append(hexVal);
            }
            //return encrypted value
            return encryptionValue.toString();
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }
}
