package dataStructures;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

public class Presentation implements Serializable {
    public String name;
    public String id;
    public PresentationType type;
    public Integer duration; // seconds
    public ArrayList<Section> sections;
    public ArrayList<Report> reports;

    public Presentation(String name, PresentationType type, Integer duration) {
        this.name = name;
        this.id = UUID.randomUUID().toString();
        this.type = type;
        this.duration = duration;
        this.sections = new ArrayList<>();
        this.reports = new ArrayList<>();
    }

    public static Presentation newInstance() {
        return new Presentation(
                "Default Presentation",
                PresentationType.INDIVIDUAL,
                180
        );
    }

    public static String toStringTime(Integer secondCount) {
        int seconds = secondCount % 60;
        int minutes = secondCount / 60;
        String secondsStr = Integer.toString(seconds);
        if (secondsStr.length() == 1) {
            secondsStr = "0" + secondsStr;
        }
        String minutesStr = Integer.toString(minutes);
        if (minutesStr.length() == 1) {
            minutesStr = "0" + minutesStr;
        }
        return minutesStr + ":" + secondsStr;
    }

    public String getDurationString() {
        return toStringTime(duration);
    }

    public ArrayList<String> getAllUserIDs() {
        ArrayList<String> userIDs = new ArrayList<>();
        Set<String> visitedIDs = new HashSet<>();
        Iterator<Section> sectionIterator = sections.iterator();
        while (sectionIterator.hasNext()) {
            String id = sectionIterator.next().userID;
            if (!visitedIDs.contains(id)) {
                visitedIDs.add(id);
                userIDs.add(id);
            }
        }
        return userIDs;
    }

    public Integer getPortionDuration(String userID) {
        Iterator<Section> sectionIterator = sections.iterator();
        Integer acc = 0;
        while (sectionIterator.hasNext()) {
            Section currentSection = sectionIterator.next();
            String id = currentSection.userID;
            if (id.equals(userID)) {
                acc += currentSection.duration;
            }
        }
        return acc;
    }

    public String getPortionDurationString(String userID) {
        return toStringTime(getPortionDuration(userID));
    }
}
