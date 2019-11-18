package dataStructures;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;


public class Section implements Serializable {
    public String sectionName;
    public String ownerName;
    public String userID;
    public String id;
    public Integer duration; // seconds
    public ArrayList<VibrationPattern> vibrationPattern;

    public Section(String sectionName, String ownerName, String userID, Integer duration) {
        this.sectionName = sectionName;
        this.ownerName = ownerName;
        this.userID = userID;
        this.duration = duration;
        this.id = UUID.randomUUID().toString();
        this.vibrationPattern = new ArrayList<>();
        vibrationPattern.add(VibrationPattern.LONG);
        vibrationPattern.add(VibrationPattern.SHORT);
        vibrationPattern.add(VibrationPattern.LONG);
    }

    public Section(String sectionName, String ownerName, String userID, Integer duration,
                   ArrayList<VibrationPattern> pattern) {
        this.sectionName = sectionName;
        this.ownerName = ownerName;
        this.userID = userID;
        this.duration = duration;
        this.id = UUID.randomUUID().toString();
        this.vibrationPattern = pattern;
    }

    public String getDurationString() {
        return Presentation.toStringTime(duration);
    }
}
