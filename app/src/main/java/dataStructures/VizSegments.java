package dataStructures;

import java.io.Serializable;

public class VizSegments implements NamedSegments, Serializable {
    private String name;
    private Float duration;
    public boolean isVacant;

    public VizSegments(String name, float duration) {
        this.name = name;
        this.duration = duration;
        this.isVacant = false;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Float getDuration() {
        return duration;
    }
}
