package dataStructures;

import java.io.Serializable;
import java.util.UUID;

public class Invitation implements Serializable {
    public Presentation presentation;
    public UUID receiverID;
    public UUID id;
    public InvitationType type;

    public Invitation(Presentation presentation, UUID receiverID) {
        this.presentation = presentation;
        this.receiverID = receiverID;
        this.type = InvitationType.NEW;
        this.id = UUID.randomUUID();
    }
}
