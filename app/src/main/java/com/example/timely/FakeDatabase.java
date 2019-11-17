package com.example.timely;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FakeDatabase {
    ArrayList<Presentation> presentations;
    public FakeDatabase() {
        Section one = new Section("One", 60, "SSS");
        Section two = new Section("Two", 120, "SLS");
        Section five = new Section("Five", 300, "LSS");
        Section[] ten = {five, two, two, one};
        Section[] fivem = {two, one, one, one};
        Section[] three = {one, one, one};
        Presentation a = new Presentation("CS465", "group", 600, ten);
        Presentation b = new Presentation("CS498HS", "solo", 300, fivem);
        Presentation c = new Presentation("Test", "solo", 180, three);
        Presentation[] defaultList = {a, b, c};
        presentations = new ArrayList<Presentation>(Arrays.asList(defaultList));
    }
}
