package dataStructures;

import java.io.Serializable;
import java.util.UUID;

public class User implements Serializable {
    public String name;
    public UUID id;

    public User(String name) {
        this.name = name;
        id = UUID.randomUUID();
    }
}
