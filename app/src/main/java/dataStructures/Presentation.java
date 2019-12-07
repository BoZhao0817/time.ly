package dataStructures;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;

public class Presentation implements Serializable {
    public String name;
    public UUID id;
    public UUID ownerID;
    public PresentationType type;
    public Integer duration; // seconds
    public ArrayList<Section> sections;
    public ArrayList<Report> reports;
    public ArrayList<GroupMember> members;


    public Presentation(String name, PresentationType type, Integer duration) {
        this.name = name;
        this.id = UUID.randomUUID();
        this.type = type;
        this.duration = duration;
        this.sections = new ArrayList<>();
        this.reports = new ArrayList<>();
        this.members = new ArrayList<>();
    }

    public static Presentation newInstance() {
        return new Presentation(
                "Default Presentation",
                PresentationType.INDIVIDUAL,
                180
        );
    }

    public static String toStringTime(Integer secondCount) {
        int seconds = secondCount % 60;
        int minutes = secondCount / 60;
        String secondsStr = Integer.toString(seconds);
        if (secondsStr.length() == 1) {
            secondsStr = "0" + secondsStr;
        }
        String minutesStr = Integer.toString(minutes);
        if (minutesStr.length() == 1) {
            minutesStr = "0" + minutesStr;
        }
        return minutesStr + ":" + secondsStr;
    }

    public String getDurationString() {
        return toStringTime(duration);
    }

    public boolean toDuration(String durationString) {
        if (durationString.length() != 5 || !durationString.contains(":")) {
            return false;
        }
        String numbers = durationString.replaceAll("\\D+","");
        if (numbers.length() != 4) {
            return false;
        }
        String[] time = durationString.split(":");
        if (time.length != 2) {
            return false;
        }
        String minutes = time[0];
        if (minutes.length() != 2) {
            return false;
        }
        String seconds = time[1];

        float prevDuration = this.duration;
        this.duration = Integer.parseInt(minutes) * 60 + Integer.parseInt(seconds);

        if ((float) this.duration < prevDuration) {
            // rescale only when smaller
            for (Section s: sections) {
                float d = s.duration;
                s.duration = (int) (d / prevDuration * (float)this.duration);
            }
            if (this.members != null && this.members.size() > 0) {
                for (GroupMember m : members) {
                    float d = m.duration;
                    m.duration = (int) (d/prevDuration * (float)this.duration);
                }
            }
        }
        return true;
    }

    public Integer getPortionDuration(UUID userID) {
        Iterator<GroupMember> sectionIterator = members.iterator();
        float acc = 0;
        while (sectionIterator.hasNext()) {
            GroupMember currentSection = sectionIterator.next();
            String id = currentSection.memberName;
            User u = FakeDatabase.getInstance().findUser(userID);
            if (id.equals(u.name)) {
                acc += currentSection.duration;
            }
        }
        return (int)acc;
    }

    public Integer getRemainingTime() {
        Integer time = duration;
        for (Section s: sections) {
            time -= s.duration;
        }
        return time;
    }

    public String getRemainingTimeString() {
        Integer time = getRemainingTime();
        if (time < 0) {
            return "-"+toStringTime(time);
        } else {
            return toStringTime(time);
        }
    }


    public String getPortionDurationString(UUID userID) {
        return toStringTime(getPortionDuration(userID));
    }

    public ArrayList<Section> getSectionsByUser(String userID) {
        ArrayList<Section> results = new ArrayList<>();
        for (Section s: sections) {
            if (s.ownerName.equals(userID)) {
                results.add(s);
            }
        }
        return results;
    }
    public void syncSections() {
        for(int i = 0; i < sections.size(); i++) {
            Section s = sections.get(i);
            GroupMember currM = null;
            for(GroupMember m : members) {
                if (m.memberName.equals(s.ownerName))
                    currM = m;
            }
            sections.set(i, new Section(s.sectionName, s.ownerName, currM.id, (int)currM.duration, s.patternID));
        }
    }
    public void syncGroup() {
        for(int i = 0; i < members.size(); i++) {
            GroupMember m = members.get(i);
            int dur = 0;
            for(Section s : sections) {
                if (s.ownerName.equals(m.memberName)) {
                    dur += s.duration;
                }
            }
            m.duration = (float) dur;
        }
    }
    public boolean isOwner() {
        return ownerID.equals(FakeDatabase.getInstance().currentUser.id);
    }
}
