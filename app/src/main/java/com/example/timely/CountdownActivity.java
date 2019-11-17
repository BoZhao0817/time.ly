package com.example.timely;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import dataStructures.Presentation;

public class CountdownActivity extends AppCompatActivity {
    private Presentation presentation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdown);
        Intent i = getIntent();
        presentation = (Presentation) i.getSerializableExtra("data");
        Log.d("PRESENTATION", presentation.id + " " + presentation.name);
        this.createActionBar();
        this.initializeText();
    }

    private void initializeText() {
        if (presentation != null) {
            TextView t = findViewById(R.id.countdown_current_section);
            t.setText(presentation.sections.get(0).sectionName);
            TextView time = findViewById(R.id.timeView);
            time.setText(Presentation.toStringTime(presentation.sections.get(0).duration));
            if (presentation.sections.size() >= 2) {
                TextView nextSection = findViewById(R.id.countdown_next_section);
                String str = "Next: " + presentation.sections.get(1).sectionName;
                nextSection.setText(str);
            }
        }
    }

    private void createActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            final Drawable backArrow = ContextCompat.getDrawable(this, R.drawable.icon_blue_arrow_back);
            actionBar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getApplicationContext(), R.color.frontLayer)));
            actionBar.setElevation(0);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(backArrow);
            actionBar.show();
        }
    }

    // this method is necessary for appbar's back button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
