package dataStructures;

import java.io.Serializable;
import java.util.UUID;

public class GroupMember implements Serializable, NamedSegments {
    public float duration;
    public String memberName;
    public UUID id;
    public UUID ownerID;
    public boolean isAccepted;

    public GroupMember(float duration, String memberName, UUID ownerID) {
        this.duration = duration;
        this.ownerID = ownerID;
        this.memberName = memberName;
        this.id = UUID.randomUUID();
        this.isAccepted = true;
    }

    public GroupMember(float duration, User user) {
        this.duration = duration;
        this.ownerID = user.id;
        this.memberName = user.name;
        this.id = UUID.randomUUID();
        this.isAccepted = true;
    }

    @Override
    public Float getDuration() {
        return duration;
    }

    @Override
    public String getName() {
        return this.memberName;
    }

    public String getDurationString() {
        return Presentation.toStringTime((int) duration);
    }

    public static GroupMember newInstance() {
        return new GroupMember(5, "Jane Doe", null);
    }
}
