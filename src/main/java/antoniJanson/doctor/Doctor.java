package antoniJanson.doctor;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Doctor {
    private int doctorId;
    private String username;
    private String password;
    private String name;
    private String surname;
    private String specialization;

    Doctor(final int doctorId, final String name, final String surname) {
        this.doctorId = doctorId;
        this.name = name;
        this.surname = surname;
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "doctorId=" + doctorId +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                '}';
    }
}
