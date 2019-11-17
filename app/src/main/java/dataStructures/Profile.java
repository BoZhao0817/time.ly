package dataStructures;

import java.util.UUID;

public class Profile {
    public String userName;
    public String userID;

    public Profile(String userName) {
        this.userName = userName;
        this.userID = UUID.randomUUID().toString();
    }
}
