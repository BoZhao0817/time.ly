package com.example.timely;

import android.app.AppComponentFactory;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import dataStructures.Presentation;
import dataStructures.User;
import dataStructures.Utilities;

public class AddGroupMemberActivity extends AppCompatActivity implements View.OnClickListener{
    EditText minutes;
    EditText seconds;
    EditText name;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_edit_member_2);
        Intent i = getIntent();
        User u = (User) i.getSerializableExtra("user");
        name = findViewById(R.id.editName);
        minutes = findViewById(R.id.minutes);
        seconds = findViewById(R.id.seconds);
        TextView save = findViewById(R.id.saveView);
        TextView delete = findViewById(R.id.deleteView);
        TextView copy = findViewById(R.id.copyView);
        TextView search = findViewById(R.id.searchView);

        save.setOnClickListener(this);
        delete.setOnClickListener(this);
        copy.setOnClickListener(this);
        search.setOnClickListener(this);

        if (u != null) {
            name.setText(u.name);
            minutes.setText(""+u.duration / 60);
            seconds.setText(""+u.duration % 60);
            if (seconds.getText().toString().length() < 2) {
                seconds.setText("0" + seconds.getText().toString());
            }
        }

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.saveView:
                int d = Integer.parseInt(minutes.getText().toString())*60 + Integer.parseInt(seconds.getText().toString());
                String n = name.getText().toString();
                break;
            case R.id.deleteView:
                break;
            case R.id.copyView:
                Toast.makeText(this, "Link Copied to Clipboard.", Toast.LENGTH_LONG).show();
                break;
            case R.id.searchView:
                Intent search = new Intent(this, SearchUserActivity.class);
                startActivityForResult(search, 1337);
                break;
        }
    }
}
