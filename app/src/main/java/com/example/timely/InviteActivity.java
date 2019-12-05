package com.example.timely;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import dataStructures.FakeDatabase;
import dataStructures.Presentation;
import dataStructures.PresentationType;
import dataStructures.Section;

public class InviteActivity extends AppCompatActivity implements View.OnClickListener {
    Button a465;
    Button c465;
    Button a411;
    Button c411;
    Button a498;
    Button c498;
    Presentation p465;
    Presentation p411;
    Presentation p498;
    int count = 3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);
        p465 = new Presentation("CS465 Final", PresentationType.GROUP, 600);
        p465.sections.add(new Section("Intro", "Emma", FakeDatabase.getInstance().users.get(1).id, 300, FakeDatabase.getInstance().vibrationPatterns.get(0).id));
        p465.sections.add(new Section("Conclusion", "me", FakeDatabase.getInstance().users.get(0).id, 300, FakeDatabase.getInstance().vibrationPatterns.get(1).id));
        p465.ownerID = FakeDatabase.getInstance().users.get(1).id;
        p411 = new Presentation("CS411 Final", PresentationType.GROUP, 300);
        p411.sections.add(new Section("Intro", "me", FakeDatabase.getInstance().users.get(0).id, 180, FakeDatabase.getInstance().vibrationPatterns.get(0).id));
        p411.sections.add(new Section("Intro", "Emma", FakeDatabase.getInstance().users.get(1).id, 120, FakeDatabase.getInstance().vibrationPatterns.get(0).id));
        p411.ownerID = FakeDatabase.getInstance().users.get(1).id;
        p498 = new Presentation("CS498 Final", PresentationType.GROUP, 720);
        p498.sections.add(new Section("Intro", "Emma", FakeDatabase.getInstance().users.get(1).id, 300, FakeDatabase.getInstance().vibrationPatterns.get(0).id));
        p498.sections.add(new Section("Content", "me", FakeDatabase.getInstance().users.get(0).id, 300, FakeDatabase.getInstance().vibrationPatterns.get(1).id));
        p498.sections.add(new Section("Conclusion", "Chris", FakeDatabase.getInstance().users.get(2).id, 120, FakeDatabase.getInstance().vibrationPatterns.get(0).id));
        p498.ownerID = FakeDatabase.getInstance().users.get(1).id;

        a465 = findViewById(R.id.accept465);
        a411 = findViewById(R.id.accept411);
        a498 = findViewById(R.id.accept498);
        c465 = findViewById(R.id.cancel465);
        c411 = findViewById(R.id.cancel411);
        c498 = findViewById(R.id.cancel498);

        a465.setOnClickListener(this);
        a411.setOnClickListener(this);
        a498.setOnClickListener(this);
        c465.setOnClickListener(this);
        c411.setOnClickListener(this);
        c498.setOnClickListener(this);

        LinearLayout ll = findViewById(R.id.invitell);
        FakeDatabase data = FakeDatabase.getInstance();
        if(data.b411) {
            ll.removeView(findViewById(R.id.card411));
            count--;
        }
        if(data.b465) {
            ll.removeView(findViewById(R.id.card465));
            count--;
        }
        if(data.b498) {
            ll.removeView(findViewById(R.id.card498));
            count--;
        }
        if(count == 0) {
            this.setContentView(R.layout.all_read);
        }
    }

    @Override
    public void onClick(View v) {
        LinearLayout ll = findViewById(R.id.invitell);
        switch(v.getId()) {
            case R.id.accept411:
                FakeDatabase.getInstance().presentations.add(p411);
                Toast.makeText(this, "Presentation Added", Toast.LENGTH_LONG).show();
                ll.removeView((View) v.getParent());
                FakeDatabase.getInstance().b411 = true;
                count--;
                break;
            case R.id.accept465:
                FakeDatabase.getInstance().presentations.add(p465);
                Toast.makeText(this, "Presentation Added", Toast.LENGTH_LONG).show();
                ll.removeView((View) v.getParent());
                FakeDatabase.getInstance().b465 = true;
                count--;
                break;
            case R.id.accept498:
                FakeDatabase.getInstance().presentations.add(p498);
                Toast.makeText(this, "Presentation Added", Toast.LENGTH_LONG).show();
                ll.removeView((View) v.getParent());
                FakeDatabase.getInstance().b498 = true;
                count--;
                break;
            case R.id.cancel411:
                ll.removeView((View) v.getParent());
                FakeDatabase.getInstance().b411 = true;
                count--;
                break;
            case R.id.cancel465:
                ll.removeView((View) v.getParent());
                FakeDatabase.getInstance().b465 = true;
                count--;
                break;
            case R.id.cancel498:
                ll.removeView((View) v.getParent());
                FakeDatabase.getInstance().b498 = true;
                count--;
                break;
        }
        if(count == 0) {
            this.setContentView(R.layout.all_read);
        }
    }
}
