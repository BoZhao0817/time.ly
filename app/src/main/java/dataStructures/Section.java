package dataStructures;

import java.io.Serializable;
import java.util.UUID;


public class Section implements Serializable {
    public String sectionName;
    public String ownerName;
    public String userID;
    public String id;
    public Integer duration; // seconds
    public String patternID;

    public Section(String sectionName, String ownerName, String userID, Integer duration) {
        this.sectionName = sectionName;
        this.ownerName = ownerName;
        this.userID = userID;
        this.duration = duration;
        this.id = UUID.randomUUID().toString();
        this.patternID = FakeDatabase.getInstance().vibrationPatterns.get(0).id;
    }

    public Section(String sectionName, String ownerName, String userID, Integer duration,
                   String patternID) {
        this.sectionName = sectionName;
        this.ownerName = ownerName;
        this.userID = userID;
        this.duration = duration;
        this.id = UUID.randomUUID().toString();
        this.patternID = patternID;
    }

    public static Section newInstance() {
        return new Section(
                "Default Section",
                FakeDatabase.getInstance().currentUser.userName,
                FakeDatabase.getInstance().currentUser.userID,
                100);
    }

    public String getDurationString() {
        return Presentation.toStringTime(duration);
    }
}
