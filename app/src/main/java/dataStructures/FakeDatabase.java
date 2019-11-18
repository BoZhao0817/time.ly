package dataStructures;

import java.util.ArrayList;

public class FakeDatabase {
    public ArrayList<Presentation> presentations;
    public ArrayList<Profile> users;
    public Profile currentUser;
    public Report testReport;
    private static FakeDatabase instance;

    private FakeDatabase() {
        this.presentations = new ArrayList<>();
        this.users = new ArrayList<>();
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

        Report r = new Report(b);
        r.addEstimate(45);
        r.addEstimate(90);
        r.addEstimate(20);
        testReport = r;
    }
}
