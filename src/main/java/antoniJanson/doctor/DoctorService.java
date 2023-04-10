package antoniJanson.doctor;

import lombok.Getter;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class DoctorService {

    @Getter
    private Doctor currentDoctor;
    private final DoctorRepository doctorRepository = new DoctorRepository();

    public boolean checkIfUsernameExists(final String username) {
        return doctorRepository.findByUsername(username).isPresent();
    }

    public boolean checkIfDoctorExists(final int doctorId) {
        return doctorRepository.findById(doctorId).isPresent();
    }

    public boolean loginAuthentication(String username, String password){
        Optional<Doctor> doctor = doctorRepository.findByUsername(username);
        if(doctor.isPresent()){
            currentDoctor = doctor.get();
            return Objects.equals(currentDoctor.getPassword(), encryptPass(password));
        }
        return false;
    }

    public Doctor createNewDoctor(String username, String password, String name, String surname, String specialization) {
        return new Doctor(doctorRepository.createDoctor(username, password, name, surname, specialization),
                username, password, name ,surname, specialization);
    }

    public List<Doctor> getAllDoctorsOfSpecialization(String specialization) {
        return doctorRepository.getAllDoctorsOfSpecialization(specialization);
    }

    public Doctor getDoctorForSurname(String surname){
        Optional<Doctor> doctor = doctorRepository.getDoctorWithSurname(surname);
        return doctor.orElse(null);
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
