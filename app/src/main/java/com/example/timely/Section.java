package com.example.timely;

public class Section {
    String name;
    Integer duration; // seconds
    String vibrationPattern; //e.g. SLSSL

    public Section(String n, Integer d, String p) {
        name = n;
        duration = d;
        vibrationPattern = p;
    }
}
