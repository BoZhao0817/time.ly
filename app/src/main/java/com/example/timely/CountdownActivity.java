package com.example.timely;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import dataStructures.Presentation;
import dataStructures.State;
import dataStructures.Timer;

public class CountdownActivity extends AppCompatActivity implements View.OnClickListener {
    private Presentation presentation;
    boolean finished = false;
    Timer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdown);
        Intent i = getIntent();
        presentation = (Presentation) i.getSerializableExtra("data");
        Log.d("PRESENTATION", presentation.id + " " + presentation.name);
        this.createActionBar();
        this.initializeText(0);
        timer = new Timer(presentation.sections.get(0).duration);
        final Handler handler = new Handler();
        final int delay = 1000;
        final TextView timeView = findViewById(R.id.countdown_current_time);
        ImageButton play = findViewById(R.id.playButton);
        ImageButton pause = findViewById(R.id.pauseButton);
        ImageButton stop = findViewById(R.id.stopButton);
        ImageButton next = findViewById(R.id.nextButton);
        play.setOnClickListener(this);
        pause.setOnClickListener(this);
        stop.setOnClickListener(this);
        next.setOnClickListener(this);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                timer.updateTimer();
                timeView.setText(timer.timeString);
                if(timer.done) {
                    if (timer.count < presentation.sections.size()) {
                        timer.seconds = presentation.sections.get(timer.count).duration;
                        timer.done = false;
                        initializeText(timer.count);
                    } else {
                        finished = true;
                    }
                }
                if (!finished) {
                    handler.postDelayed(this, delay);
                } else {

                }
            }
        }, delay);

    }

    private void initializeText(int curr) {
        if (presentation != null) {
            TextView t = findViewById(R.id.countdown_current_section);
            t.setText(presentation.sections.get(curr).sectionName);
            TextView time = findViewById(R.id.countdown_current_time);
            time.setText(Presentation.toStringTime(presentation.sections.get(curr).duration));
            if (presentation.sections.size() > curr + 1) {
                TextView nextSection = findViewById(R.id.countdown_next_section);
                String str = "Next: " + presentation.sections.get(curr+1).sectionName;
                nextSection.setText(str);
            } else {
                TextView nextSection = findViewById(R.id.countdown_next_section);
                String str = "Next: End of Presentation";
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

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pauseButton:
                timer.state = State.PAUSED;
                break;
            case R.id.playButton:
                timer.state = State.PLAYING;
                break;
            case R.id.stopButton:
                finished = true;
            case R.id.nextButton:
                timer.count++;
                timer.done = true;
            default:
                break;
        }
    }
}
