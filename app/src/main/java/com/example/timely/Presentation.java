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
    public String type;
    public Integer duration; // seconds
    public Section[] sections;

    public String getDuration() {
        return duration.toString();
    }

    public Presentation(String n, String t, Integer d, Section[] s) {
        name = n;
        type = t;
        duration = d;
        sections = s;
    }
}
