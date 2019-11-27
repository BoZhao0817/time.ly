package dataStructures;

import java.io.Serializable;
import java.util.UUID;


public class Section implements Serializable, NamedSegments {
    public String sectionName;
    public String ownerName;
    public UUID userID;
    public UUID id;
    public Integer duration; // seconds
    public UUID patternID;

//    public Section(String sectionName, String ownerName, String userID, Integer duration) {
//        this.sectionName = sectionName;
//        this.ownerName = ownerName;
//        this.userID = userID;
//        this.duration = duration;
//        this.id = UUID.randomUUID().toString();
//        this.patternID = FakeDatabase.getInstance().vibrationPatterns.get(0).id;
//    }

    public Section(String sectionName, String ownerName, UUID userID, Integer duration,
                   UUID patternID) {
        this.sectionName = sectionName;
        this.ownerName = ownerName;
        this.userID = userID;
        this.duration = duration;
        this.id = UUID.randomUUID();
        this.patternID = patternID;
    }

    public static Section newInstance() {
        return new Section(
                "Default Section",
                FakeDatabase.getInstance().currentUser.name,
                FakeDatabase.getInstance().currentUser.id,
                100,
                FakeDatabase.getInstance().vibrationPatterns.get(0).id);
    }

    public String getDurationString() {
        return Presentation.toStringTime(duration);
    }

    @Override
    public String getName() { return this.sectionName; }

    @Override
    public Float getDuration() {
        return duration.floatValue();
    }
}
