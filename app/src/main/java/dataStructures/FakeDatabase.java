package dataStructures;

import java.util.ArrayList;

public class FakeDatabase {
    public ArrayList<Presentation> presentations;
    public ArrayList<Profile> users;
    public Profile currentUser;
    public Report testReport;
    public ArrayList<ArrayList<VibrationPattern> > aList = new ArrayList<ArrayList<VibrationPattern> >();
    private static FakeDatabase instance;
    private FakeDatabase() {
        this.presentations = new ArrayList<>();
        this.users = new ArrayList<>();
        this.aList= new ArrayList<ArrayList<VibrationPattern> >();
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

        Presentation a = new Presentation("CS465", PresentationType.GROUP, 600);
        a.sections.add(new Section("a1", users.get(0).userName, users.get(0).userID, 60));
        a.sections.add(new Section("a2", users.get(1).userName, users.get(1).userID, 100));
        a.sections.add(new Section("a3", users.get(2).userName, users.get(2).userID, 10));

        Presentation b = new Presentation("CS498", PresentationType.INDIVIDUAL, 120);
        b.sections.add(new Section("b1", users.get(0).userName, users.get(0).userID, 10));
        b.sections.add(new Section("b2", users.get(0).userName, users.get(0).userID, 15));
        b.sections.add(new Section("b3", users.get(0).userName, users.get(0).userID, 10));

        Presentation c = new Presentation("CS101", PresentationType.INDIVIDUAL, 60);
        c.sections.add(new Section("c1", users.get(0).userName, users.get(0).userID, 60));

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

        ArrayList<VibrationPattern> pattern1= new ArrayList<>();
        pattern1.add(VibrationPattern.SHORT);
        //pattern1.add(VibrationPattern.LONG);
        //pattern1.add(VibrationPattern.SHORT);
        aList.add(pattern1);
        ArrayList<VibrationPattern> pattern2= new ArrayList<>();
        pattern2.add(VibrationPattern.LONG);
        aList.add(pattern2);
        ArrayList<VibrationPattern> pattern3= new ArrayList<>();
        pattern3.add(VibrationPattern.SHORT);
        pattern3.add(VibrationPattern.SHORT);
        pattern3.add(VibrationPattern.LONG);
        aList.add(pattern3);
    }
}