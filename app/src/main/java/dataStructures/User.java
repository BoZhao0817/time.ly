package dataStructures;

import java.io.Serializable;
import java.util.UUID;

public class User implements Serializable {
    public String name;
    public UUID id;
    public int duration;
    public String role;
    public User(String n, int d, String r) {
        name = n;
        id = UUID.randomUUID();
        duration = d;
        role = r;
    }
}
