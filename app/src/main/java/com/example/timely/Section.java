package com.example.timely;

import java.io.Serializable;

enum VibrationPattern {
    LONG, SHORT
}

public class Section implements Serializable {
    String identifier;
    String owner;
    String id;
    Integer duration; // seconds
    VibrationPattern[] vibrationPattern;
}
