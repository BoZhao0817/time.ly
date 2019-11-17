package dataStructures;

import java.io.Serializable;
import java.util.ArrayList;


public class Section implements Serializable {
    public String name;
    public String owner;
    public String userID;
    public String id;
    public Integer duration; // seconds
    public ArrayList<VibrationPattern> vibrationPattern;

    public String getDurationString(String userID) {
        return Presentation.toStringTime(duration);
    }
}
