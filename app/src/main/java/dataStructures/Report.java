package dataStructures;

import java.util.ArrayList;
import java.util.UUID;

public class Report {
    public UUID id;
    public int total_estimate;
    public int total_actual;
    public ArrayList<Integer> estimates;
    public ArrayList<Integer> actuals;

    public Report(Presentation p) {
        id = UUID.randomUUID();
        total_actual = p.duration;
        for (Section s : p.sections) {
            actuals.add(s.duration);
        }
    }

    public void addEstimate(int dur) {
        estimates.add(dur);
        total_estimate += dur;
    }
}