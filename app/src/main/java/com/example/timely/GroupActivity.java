package com.example.timely;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import dataStructures.Group;
import dataStructures.User;
import dataStructures.Utilities;

public class GroupActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_status);
        Group testGroup = new Group();
        User u1 = new User("Zach", 60, "Organizer");
        User u2 = new User("Hang", 30, "Member");
        User u3 = new User("Bo", 45, "Member");
        User u4 = new User("Samuel", 20, "Member");
        User u5 = new User("Aditi", 60, "Member");
        User u6 = new User("Harshit", 80, "Member");
        testGroup.users.add(u1);
        testGroup.users.add(u2);
        testGroup.users.add(u3);
        testGroup.users.add(u4);
        testGroup.users.add(u5);
        testGroup.users.add(u6);

        TextView add = findViewById(R.id.add_new_member);
        Button addButton = findViewById(R.id.add_member);

        add.setOnClickListener(this);
        addButton.setOnClickListener(this);

        ListView users = findViewById(R.id.userList);
        GroupAdapter adapter = new GroupAdapter(this, testGroup.users);
        users.setAdapter(adapter);

        LinearLayout glance = findViewById(R.id.ll1);
        Utilities util = new Utilities(getApplicationContext());
        ArrayList<Integer> times = new ArrayList<>();
        for (User u : testGroup.users) {
            times.add(u.duration);
        }
        util.setChart(glance, times, 350);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_new_member:
            case R.id.add_member:
                Intent i = new Intent(this, AddGroupMemberActivity.class);
                startActivity(i);
                break;
            default:
                break;
        }
    }
}
