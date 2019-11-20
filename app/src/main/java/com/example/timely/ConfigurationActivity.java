package com.example.timely;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import dataStructures.FakeDatabase;
import dataStructures.Presentation;
import dataStructures.Section;
import dataStructures.Utilities;

public class ConfigurationActivity extends AppCompatActivity {
    Presentation activePresentation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);
        createActionBar();
        Intent i = getIntent();
        activePresentation = (Presentation) i.getSerializableExtra("data");
        Utilities utilities=new Utilities(getApplicationContext());
        TextView total_time = findViewById(R.id.total_time);
        ListView lView = findViewById(R.id.listview);
        LinearLayout linearLayout1=findViewById(R.id.ll1);
        Button add_section= findViewById(R.id.add_section);
        ArrayList<Section> list = new ArrayList<>();
        ArrayList<Integer>array1=new ArrayList<>();
        int total_duration=0;
        for(Section temp:activePresentation.sections)
        {
            list.add(temp);
            total_duration+=temp.duration;
            array1.add(temp.duration);
        }
        SectionAdapter adapter = new SectionAdapter(list, this, activePresentation);
        utilities.setChart(linearLayout1,array1,320);
        lView.setAdapter(adapter);
        total_time.setText(activePresentation.getDurationString());
        add_section.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent add_new_section = new Intent(getApplicationContext(), EditSection.class);
                add_new_section.putExtra("data", activePresentation);
                add_new_section.putExtra("user_name",FakeDatabase.getInstance().users.get(0).userName);
                add_new_section.putExtra("user_id",FakeDatabase.getInstance().users.get(0).userID);
                startActivity(add_new_section);
                // Do something
            }
        });
    }
    @Override
    public void onBackPressed() {
        Intent main_page = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(main_page);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.config_menu, menu);
        return true;
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
