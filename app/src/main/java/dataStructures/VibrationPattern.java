package dataStructures;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

public class VibrationPattern implements Serializable {
    public String id;
    public String name;
    public ArrayList<VibrationPatternType> patterns;


    public VibrationPattern(String name) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
    }
}
