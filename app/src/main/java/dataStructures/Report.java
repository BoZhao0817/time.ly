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
    public ArrayList<NamedSegments> estimates;
    public ArrayList<NamedSegments> actuals;
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
        actuals = new ArrayList<>();
        actuals.addAll(p.sections);
    }

    public void addEstimate(String name, float duration) {
        estimates.add(new VizSegments(name, duration));
        total_estimate += duration;
    }

    public String getDurationString() {
        return Presentation.toStringTime(total_actual);
    }
}
