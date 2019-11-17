package dataStructures;

import androidx.annotation.NonNull;

public enum PresentationType {
    INDIVIDUAL, GROUP;

    @NonNull
    @Override
    public String toString() {
        return name().substring(0, 1).toUpperCase() + name().substring(1).toLowerCase();
    }
}