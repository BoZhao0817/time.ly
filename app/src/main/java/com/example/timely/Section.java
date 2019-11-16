package com.example.timely;

enum VibrationPattern {
    LONG, SHORT
}

public class Section {
    String name;
    Integer duration; // seconds
    VibrationPattern[] vibrationPattern;
}
