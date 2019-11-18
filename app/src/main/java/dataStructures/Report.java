package dataStructures;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class Report implements Serializable {
    public UUID id;
    public Date date;
    public int total_estimate;
    public int total_actual;
    public ArrayList<Integer> estimates;
    public ArrayList<Integer> actuals;
    public PresentationType type;

    public Report(Presentation p) {
        id = UUID.randomUUID();
        date = new Date();
        total_actual = p.duration;
        actuals = new ArrayList<>();
        estimates = new ArrayList<>();
        type = p.type;
        for (Section s : p.sections) {
            actuals.add(s.duration);
        }
    }

    public void addEstimate(int dur) {
        estimates.add(dur);
        total_estimate += dur;
    }
}
