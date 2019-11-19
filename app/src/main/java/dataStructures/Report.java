package dataStructures;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class Report implements Serializable {
    public UUID id;
    public Date date;
    public String name;
    public int total_estimate;
    public int total_actual;
    public ArrayList<Integer> estimates;
    public ArrayList<Integer> actuals;
    public PresentationType type;
    public ReportGroupType group_type;

    public Report(Presentation p, String name) {
        id = UUID.randomUUID();
        this.name = name;
        date = new Date();
        total_actual = p.duration;
        actuals = new ArrayList<>();
        estimates = new ArrayList<>();
        type = p.type;
        group_type = null;
        for (Section s : p.sections) {
            actuals.add(s.duration);
        }
    }

    public void addEstimate(int dur) {
        estimates.add(dur);
        total_estimate += dur;
    }

    public String getDurationString() {
        return Presentation.toStringTime(total_actual);
    }
}
