package com.example.timely;

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

public class Presentation {
    public String name;
    public PresentationType type;
    public Integer duration; // seconds
    public Section[] sections;

    public String getDuration() {
        return duration.toString();
    }
}
