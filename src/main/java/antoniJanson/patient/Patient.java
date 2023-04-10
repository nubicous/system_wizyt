package antoniJanson.patient;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Patient {
    private int patientId;
    private String username;
    private String password;
    private String name;
    private String surname;
}
