package com.example.timely;

import java.io.Serializable;
import java.util.UUID;

enum PresentationType {
    INDIVIDUAL("Individual"), GROUP("Group");
    private String val;

    PresentationType(String val) {
        this.val = val;
    }

    public String getVal() {
        return val;
    }
}

public class Presentation implements Serializable {
    public String name;
    public String id;
    public PresentationType type;
    public Integer duration; // seconds
    public Section[] sections;

    public String getDuration() {
        Integer seconds = duration % 60;
        Integer minutes = duration / 60;
        String secondsStr = seconds.toString();
        if (secondsStr.length() == 1) {
            secondsStr = "0" + secondsStr;
        }
        String minutesStr = minutes.toString();
        if (minutesStr.length() == 1) {
            minutesStr = "0" + minutesStr;
        }
        return minutesStr + ":" + secondsStr;
    }

    public static Presentation newInstance() {
        Presentation datum = new Presentation();
        datum.name = "Default Presentation";
        datum.duration = 180;
        datum.type = PresentationType.INDIVIDUAL;
        datum.id = UUID.randomUUID().toString();
        return datum;
    }
}
