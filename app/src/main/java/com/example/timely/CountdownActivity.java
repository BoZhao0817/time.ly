package com.example.timely;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import dataStructures.Presentation;

public class CountdownActivity extends AppCompatActivity {
    private Presentation presentation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdown);
        Intent i = getIntent();
        presentation = (Presentation) i.getSerializableExtra("data");

        this.initializeText();
    }

    private void initializeText() {
        if (presentation != null) {
            TextView t = findViewById(R.id.countdown_current_section);
            t.setText(presentation.sections.get(0).getDurationString());
            if (presentation.sections.size() >= 2) {
                TextView nextSection = findViewById(R.id.countdown_next_section);
                String str = "Next: " + presentation.sections.get(1).sectionName;
                nextSection.setText(str);
            }
        }
    }
}
