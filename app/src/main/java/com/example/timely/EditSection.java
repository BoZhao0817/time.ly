package com.example.timely;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import dataStructures.FakeDatabase;
import dataStructures.Section;
import dataStructures.VibrationPattern;

public class EditSection extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_section);
        createActionBar();
        final Intent intent=getIntent();
        final EditText section_name = (EditText) findViewById(R.id.sec_name);
        final EditText section_duration = (EditText) findViewById(R.id.sec_duration);
        final TextView selectedPattern= (TextView)findViewById(R.id.selectedPattern);
        ListView lView = (ListView)findViewById(R.id.listview2);
        section_name.setText(intent.getStringExtra("section_name"));
        section_duration.setText(intent.getStringExtra("section_duration"));
        Button delete_button= findViewById(R.id.DeleteSection);
        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent go_back = new Intent(getApplicationContext(), ConfigurationActivity.class);
                FakeDatabase.getInstance().presentations.get(1).sections.remove(intent.getIntExtra("section_index",0));
                startActivity(go_back);
            }
        });
        PresetAdapter adapter = new PresetAdapter(FakeDatabase.getInstance().aList,selectedPattern, this);
        lView.setAdapter(adapter);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_section_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.mybutton) {
            final Intent intent=getIntent();
            final EditText section_name = (EditText) findViewById(R.id.sec_name);
            final EditText section_duration = (EditText) findViewById(R.id.sec_duration);
            if(intent.getStringExtra("section_name")==null)
            {
                FakeDatabase.getInstance().presentations.get(1).sections.add(new Section
                        (section_name.getText().toString(), intent.getStringExtra("user_name"),
                                intent.getStringExtra("user_id"), Integer.parseInt(section_duration.getText().toString())));
                Intent go_back = new Intent(getApplicationContext(), ConfigurationActivity.class);
                startActivity(go_back);
            }
            else {
                FakeDatabase.getInstance().presentations.get(1).sections.set(intent.getIntExtra
                        ("section_index", 0), new Section
                        (section_name.getText().toString(), intent.getStringExtra("user_name"),
                                intent.getStringExtra("user_id"), Integer.parseInt(section_duration.getText().toString())));
                Intent go_back = new Intent(getApplicationContext(), ConfigurationActivity.class);
                startActivity(go_back);
            }

        }
        return super.onOptionsItemSelected(item);
    }
    private void createActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getApplicationContext(), R.color.appBar)));
            actionBar.setElevation(0);
            actionBar.show();
        }
    }

}

