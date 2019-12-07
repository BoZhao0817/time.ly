package dataStructures;

import java.util.ArrayList;
import java.util.UUID;

public class FakeDatabase {
    public ArrayList<Presentation> presentations;
    public ArrayList<User> users;
    public ArrayList<VibrationPattern> vibrationPatterns;
    public ArrayList<Invitation> invitations;
    public User currentUser;
    public Report testReport;
    private static FakeDatabase instance;
    private FakeDatabase() {
        this.presentations = new ArrayList<>();
        this.users = new ArrayList<>();
        this.vibrationPatterns= new ArrayList<>();
        this.invitations = new ArrayList<>();
        populateData();
    }
    // add synchronized for multi-threading
    public static FakeDatabase getInstance()
    {
        if (instance == null) {
            instance = new FakeDatabase();
        }
        return instance;
    }

    private void populateData() {
        users.add(new User("me"));
        users.add(new User("Emma"));
        users.add(new User("Chris"));

        currentUser = users.get(0);

        VibrationPattern vp1 = new VibrationPattern("Pattern 1");
        vp1.patterns.add(VibrationPatternType.SHORT);

        VibrationPattern vp2 = new VibrationPattern("Pattern 2");
        vp2.patterns.add(VibrationPatternType.LONG);

        VibrationPattern vp3 = new VibrationPattern("Pattern 3");
        vp3.patterns.add(VibrationPatternType.SHORT);
        vp3.patterns.add(VibrationPatternType.LONG);
        vp3.patterns.add(VibrationPatternType.SHORT);

        vibrationPatterns.add(vp1);
        vibrationPatterns.add(vp2);
        vibrationPatterns.add(vp3);

        Presentation a = new Presentation("CS465", PresentationType.GROUP, 600);
        a.ownerID = currentUser.id;
        a.sections.add(new Section("Intro", users.get(0).name, users.get(0).id, 60, vp1.id));
        a.sections.add(new Section("Content", users.get(1).name, users.get(1).id, 100, vp2.id));
        a.sections.add(new Section("Conclusion", users.get(2).name, users.get(2).id, 10, vp3.id));
        a.members.add(new GroupMember(60, users.get(0)));
        a.members.add(new GroupMember(100, users.get(1)));
        a.members.add(new GroupMember(10, users.get(2)));

        Presentation b = new Presentation("CS498", PresentationType.INDIVIDUAL, 120);
        b.ownerID = currentUser.id;
        b.sections.add(new Section("Start", users.get(0).name, users.get(0).id, 10, vp1.id));
        b.sections.add(new Section("Middle", users.get(0).name, users.get(0).id, 15, vp2.id));
        b.sections.add(new Section("End", users.get(0).name, users.get(0).id, 10, vp2.id));
        b.members.add(new GroupMember(120, users.get(0)));

        Presentation c = new Presentation("CS101", PresentationType.INDIVIDUAL, 60);
        c.ownerID = currentUser.id;
        c.sections.add(new Section("Minute Pres.", users.get(0).name, users.get(0).id, 60, vp1.id));
        c.members.add(new GroupMember(60, users.get(0)));

        presentations.add(a);
        presentations.add(b);
        presentations.add(c);

        Report r = new Report(b, "existing recording");
        r.addEstimate(b.sections.get(0).sectionName, 45);
        r.addEstimate(b.sections.get(1).sectionName, 90);
        r.addEstimate(b.sections.get(2).sectionName, 20);
        r.group_type = ReportGroupType.GROUP;
        r.type = PresentationType.GROUP;
        a.reports.add(r);

        Report br = new Report(b, "Test B1");
        br.addEstimate(b.sections.get(0).sectionName, 45);
        br.addEstimate(b.sections.get(1).sectionName, 90);
        br.addEstimate(b.sections.get(2).sectionName, 20);

        Report br2 = new Report(b, "Test B2");
        br2.addEstimate(b.sections.get(0).sectionName, 50);
        br2.addEstimate(b.sections.get(1).sectionName, 100);
        br2.addEstimate(b.sections.get(2).sectionName, 40);
        testReport = br;

        Report ar = new Report(a, "Test A1");
        ar.addEstimate(a.sections.get(0).sectionName, 70);
        ar.addEstimate(a.sections.get(1).sectionName, 110);
        ar.addEstimate(a.sections.get(2).sectionName, 20);
        ar.group_type = ReportGroupType.GROUP;

        Report ar2 = new Report(a, "Test A2");
        ar2.addEstimate(a.sections.get(0).sectionName, 70);
        ar2.addEstimate(a.sections.get(1).sectionName, 110);
        ar2.addEstimate(a.sections.get(2).sectionName, 20);
        ar2.group_type = ReportGroupType.GROUP;

        Report cr = new Report(c, "Test C1");
        cr.addEstimate(c.sections.get(0).sectionName, 80);

        b.reports.add(br);
        b.reports.add(br2);

        a.reports.add(ar);
        a.reports.add(ar2);

        c.reports.add(cr);

        // add invitations
        Presentation p465 = new Presentation("IE465 Final", PresentationType.GROUP, 600);
        p465.sections.add(new Section("Intro", "Emma", users.get(1).id, 300, vibrationPatterns.get(0).id));
        p465.sections.add(new Section("Conclusion", "me", users.get(0).id, 300, vibrationPatterns.get(1).id));
        p465.ownerID = users.get(1).id;
        Presentation p411 = new Presentation("IE411 Final", PresentationType.GROUP, 300);
        p411.sections.add(new Section("Intro", "me", users.get(0).id, 180, vibrationPatterns.get(0).id));
        p411.sections.add(new Section("Content", "Emma", users.get(1).id, 120, vibrationPatterns.get(0).id));
        p411.ownerID = users.get(0).id;
        Presentation p498 = new Presentation("IE498 Final", PresentationType.GROUP, 720);
        p498.sections.add(new Section("Intro", "Emma", users.get(1).id, 300, vibrationPatterns.get(0).id));
        p498.sections.add(new Section("Content", "me", users.get(0).id, 300, vibrationPatterns.get(1).id));
        p498.sections.add(new Section("Conclusion", "Chris", users.get(2).id, 120, vibrationPatterns.get(0).id));
        p498.ownerID = users.get(1).id;

        invitations.add(new Invitation(p465, currentUser.id));
        invitations.add(new Invitation(p411, users.get(1).id));
        invitations.add(new Invitation(p498, currentUser.id));

        invitations.get(1).type = InvitationType.DECLINED;
    }

    public VibrationPattern findPattern(UUID id) {
        for (VibrationPattern pattern : vibrationPatterns) {
            if (pattern.id.equals(id)) {
                return pattern;
            }
        }
        return null;
    }

    public User findUser(UUID id) {
        for (User user : users) {
            if (user.id.equals(id)) {
                return user;
            }
        }
        return null;
    }

    public ArrayList<User> findUser(String name) {
        ArrayList<User> results = new ArrayList<>();
        for (User user : users) {
            if (user.name.toLowerCase().contains(name.toLowerCase())) {
                results.add(user);
            }
        }
        return results;
    }

    public Presentation findPresentation(UUID id) {
        for (Presentation p : presentations) {
            if (p.id.equals(id)) {
                return p;
            }
        }
        return null;
    }

    public int getInvitationIndex(UUID id) {
        for (int i = 0; i < invitations.size(); i += 1) {
            if (invitations.get(i).id.equals(id)) {
                return i;
            }
        }
        return -1;
    }
}