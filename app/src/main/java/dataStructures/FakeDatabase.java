package dataStructures;

import java.util.ArrayList;

public class FakeDatabase {
    public ArrayList<Presentation> presentations;
    public ArrayList<Profile> users;
    public ArrayList<VibrationPattern> vibrationPatterns;
    public Profile currentUser;
    public Report testReport;

    private static FakeDatabase instance;
    private FakeDatabase() {
        this.presentations = new ArrayList<>();
        this.users = new ArrayList<>();
        this.vibrationPatterns= new ArrayList<>();
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
        users.add(new Profile("me"));
        users.add(new Profile("Emma"));
        users.add(new Profile("Chris"));

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
        a.sections.add(new Section("a1", users.get(0).userName, users.get(0).userID, 60, vp1.id));
        a.sections.add(new Section("a2", users.get(1).userName, users.get(1).userID, 100, vp2.id));
        a.sections.add(new Section("a3", users.get(2).userName, users.get(2).userID, 10, vp3.id));

        Presentation b = new Presentation("CS498", PresentationType.INDIVIDUAL, 120);
        b.sections.add(new Section("b1", users.get(0).userName, users.get(0).userID, 10, vp1.id));
        b.sections.add(new Section("b2", users.get(0).userName, users.get(0).userID, 15, vp2.id));
        b.sections.add(new Section("b3", users.get(0).userName, users.get(0).userID, 10, vp2.id));

        Presentation c = new Presentation("CS101", PresentationType.INDIVIDUAL, 60);
        c.sections.add(new Section("c1", users.get(0).userName, users.get(0).userID, 60, vp1.id));

        presentations.add(a);
        presentations.add(b);
        presentations.add(c);

        Report r = new Report(b, "existing recording");
        r.addEstimate(45);
        r.addEstimate(90);
        r.addEstimate(20);
        r.group_type = ReportGroupType.GROUP;
        a.reports.add(r);

        Report br = new Report(b, "Test B1");
        br.addEstimate(45);
        br.addEstimate(90);
        br.addEstimate(20);

        Report br2 = new Report(b, "Test B2");
        br2.addEstimate(50);
        br2.addEstimate(100);
        br2.addEstimate(40);
        testReport = br;

        Report ar = new Report(a, "Test A1");
        ar.addEstimate(70);
        ar.addEstimate(110);
        ar.addEstimate(20);
        ar.group_type = ReportGroupType.GROUP;

        Report ar2 = new Report(a, "Test A2");
        ar2.addEstimate(50);
        ar2.addEstimate(90);
        ar2.addEstimate(5);
        ar2.group_type = ReportGroupType.GROUP;

        Report cr = new Report(c, "Test C1");
        cr.addEstimate(80);

        b.reports.add(br);
        b.reports.add(br2);

        a.reports.add(ar);
        a.reports.add(ar2);

        c.reports.add(cr);
    }

    public VibrationPattern findPattern(String id) {
        for (VibrationPattern pattern : vibrationPatterns) {
            if (pattern.id.equals(id)) {
                return pattern;
            }
        }
        return null;
    }
}