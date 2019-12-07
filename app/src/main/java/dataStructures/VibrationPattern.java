package dataStructures;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

public class VibrationPattern implements Serializable {
    public UUID id;
    public String name;
    public ArrayList<VibrationPatternType> patterns;


    public VibrationPattern(String name) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.patterns = new ArrayList<>();
    }

    public static VibrationPattern newInstance() {
        VibrationPattern vp = new VibrationPattern("Default Pattern");
        vp.patterns.add(VibrationPatternType.SHORT);
        return vp;
    }
}
