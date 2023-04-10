package antoniJanson.visit;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Date;
import java.sql.Time;

@Getter
@AllArgsConstructor
public class Visit {
    private int visit_id;
    private int doctor_id;
    private int patient_id;
    private String visit_type;
    private String dateOfVisit;
    private String timeOfVisit;

    @Override
    public String toString() {
        return "Visit{" +
                "visit_id=" + visit_id +
                ", doctor_id=" + doctor_id +
                ", patient_id=" + patient_id +
                ", visit_type='" + visit_type + '\'' +
                ", dateOfVisit=" + dateOfVisit +
                ", timeOfVisit=" + timeOfVisit +
                '}';
    }
}
